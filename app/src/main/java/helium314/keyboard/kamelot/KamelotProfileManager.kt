// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import helium314.keyboard.latin.utils.prefs
import kotlinx.serialization.json.Json

class KamelotProfileManager @JvmOverloads constructor(
    private val context: Context,
    private val prefs: SharedPreferences = context.prefs(),
) {
    companion object {
        const val PREF_PROFILES_JSON = "kamelot_profiles_json"
        const val PREF_ACTIVE_PROFILE_ID = "kamelot_active_profile_id"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun loadProfiles(): List<KamelotProfile> {
        val stored = prefs.getString(PREF_PROFILES_JSON, null)
        val profiles = stored?.let {
            runCatching { json.decodeFromString<List<KamelotProfile>>(it) }.getOrNull()
        }.orEmpty()
        return if (profiles.isEmpty()) {
            KamelotDefaults.profilePresets.also {
                saveProfiles(it)
                switchProfile(KamelotDefaults.DEFAULT_PROFILE_ID)
            }
        } else {
            if (!prefs.contains(ThemePresetMapper.PREF_ACTIVE_THEME_PRESET_ID)) {
                val activeId = prefs.getString(PREF_ACTIVE_PROFILE_ID, KamelotDefaults.DEFAULT_PROFILE_ID)
                val activeProfile = profiles.firstOrNull { it.id == activeId }
                    ?: profiles.firstOrNull { it.id == KamelotDefaults.DEFAULT_PROFILE_ID }
                    ?: profiles.first()
                ThemePresetMapper.applyPreset(context, activeProfile.themePreset, prefs)
            }
            profiles
        }
    }

    fun saveProfiles(profiles: List<KamelotProfile>) {
        val safeProfiles = profiles.ifEmpty { KamelotDefaults.profilePresets }
        prefs.edit {
            putString(PREF_PROFILES_JSON, json.encodeToString(safeProfiles))
        }
        val activeProfileId = prefs.getString(PREF_ACTIVE_PROFILE_ID, KamelotDefaults.DEFAULT_PROFILE_ID)
        if (safeProfiles.none { it.id == activeProfileId }) {
            switchProfile(KamelotDefaults.DEFAULT_PROFILE_ID)
        }
    }

    fun getActiveProfile(): KamelotProfile {
        val profiles = loadProfiles()
        val activeId = prefs.getString(PREF_ACTIVE_PROFILE_ID, KamelotDefaults.DEFAULT_PROFILE_ID)
        return profiles.firstOrNull { it.id == activeId }
            ?: profiles.firstOrNull { it.id == KamelotDefaults.DEFAULT_PROFILE_ID }
            ?: profiles.first()
    }

    fun getMacroMap(profile: KamelotProfile = getActiveProfile()): Map<String, KamelotMacro> =
        profile.macros.associateBy { it.id }

    fun updateProfile(profileId: String, transform: (KamelotProfile) -> KamelotProfile): KamelotProfile {
        val currentProfiles = loadProfiles()
        val target = currentProfiles.firstOrNull { it.id == profileId }
            ?: currentProfiles.firstOrNull { it.id == KamelotDefaults.DEFAULT_PROFILE_ID }
            ?: currentProfiles.first()
        val updatedProfiles = currentProfiles.map { profile ->
            if (profile.id == target.id) normalizeProfile(transform(profile)) else profile
        }
        saveProfiles(updatedProfiles)
        val updated = updatedProfiles.first { it.id == target.id }
        if (updated.id == prefs.getString(PREF_ACTIVE_PROFILE_ID, KamelotDefaults.DEFAULT_PROFILE_ID)) {
            ThemePresetMapper.applyPreset(context, updated.themePreset, prefs)
        }
        return updated
    }

    fun updateActiveProfile(transform: (KamelotProfile) -> KamelotProfile): KamelotProfile =
        updateProfile(getActiveProfile().id, transform)

    fun createProfile(name: String, baseProfileId: String = getActiveProfile().id): KamelotProfile {
        val base = loadProfiles().firstOrNull { it.id == baseProfileId } ?: getActiveProfile()
        val profile = normalizeProfile(
            base.copy(
                id = nextProfileId(name),
                name = nextProfileName(name),
            )
        )
        saveProfiles(loadProfiles() + profile)
        switchProfile(profile.id)
        return profile
    }

    fun renameProfile(profileId: String, name: String): KamelotProfile =
        updateProfile(profileId) { profile -> profile.copy(name = name.trim().ifBlank { profile.name }) }

    fun deleteProfile(profileId: String): Boolean {
        if (profileId in defaultProfileIds()) return false
        val currentProfiles = loadProfiles()
        if (currentProfiles.size <= 1) return false
        val updated = currentProfiles.filterNot { it.id == profileId }
        if (updated.size == currentProfiles.size) return false
        saveProfiles(updated)
        if (prefs.getString(PREF_ACTIVE_PROFILE_ID, KamelotDefaults.DEFAULT_PROFILE_ID) == profileId) {
            switchProfile(KamelotDefaults.DEFAULT_PROFILE_ID)
        }
        return true
    }

    fun switchProfile(profileId: String): KamelotProfile {
        val profile = loadProfiles().firstOrNull { it.id == profileId }
            ?: KamelotDefaults.profilePresets.first { it.id == KamelotDefaults.DEFAULT_PROFILE_ID }
        prefs.edit { putString(PREF_ACTIVE_PROFILE_ID, profile.id) }
        ThemePresetMapper.applyPreset(context, profile.themePreset, prefs)
        return profile
    }

    fun updateActiveProfileThemePreset(themePresetId: String): KamelotProfile {
        val current = getActiveProfile()
        val updatedProfiles = loadProfiles().map { profile ->
            if (profile.id == current.id) profile.copy(
                themePreset = ThemePresetMapper.getPreset(themePresetId).id,
                appearanceConfig = appearanceConfigForPreset(ThemePresetMapper.getPreset(themePresetId)),
            )
            else profile
        }
        saveProfiles(updatedProfiles)
        val updated = updatedProfiles.first { it.id == current.id }
        prefs.edit { putString(PREF_ACTIVE_PROFILE_ID, updated.id) }
        ThemePresetMapper.applyPreset(context, updated.themePreset, prefs)
        return updated
    }

    fun updateActiveProfileLayoutMode(layoutMode: FutureLayoutMode): KamelotProfile =
        updateActiveProfile { profile ->
            profile.copy(layoutMode = layoutMode)
        }

    fun updateActiveAppearanceConfig(transform: (KamelotAppearanceConfig) -> KamelotAppearanceConfig): KamelotProfile =
        updateActiveProfile { profile ->
            profile.copy(appearanceConfig = transform(profile.appearanceConfig))
        }

    fun upsertMacro(profileId: String = getActiveProfile().id, macro: KamelotMacro): KamelotProfile =
        updateProfile(profileId) { profile ->
            val updatedMacros = profile.macros.filterNot { it.id == macro.id } + macro
            val hasVisibleItem = profile.quickActionsConfig.visibleMacros.any { it.macroId == macro.id }
            profile.copy(
                macros = updatedMacros.sortedBy { it.label.lowercase() },
                quickActionsConfig = if (hasVisibleItem) profile.quickActionsConfig else {
                    val nextOrder = nextOrder(profile.quickActionsConfig)
                    profile.quickActionsConfig.copy(
                        visibleMacros = profile.quickActionsConfig.visibleMacros + MacroStripItem(
                            macroId = macro.id,
                            order = nextOrder,
                        )
                    )
                }
            )
        }

    fun deleteMacro(profileId: String = getActiveProfile().id, macroId: String): KamelotProfile =
        updateProfile(profileId) { profile ->
            profile.copy(
                macros = profile.macros.filterNot { it.id == macroId },
                quickActionsConfig = profile.quickActionsConfig.copy(
                    visibleMacros = profile.quickActionsConfig.visibleMacros.filterNot { it.macroId == macroId }
                )
            )
        }

    fun updateActiveQuickActionsConfig(transform: (QuickActionsConfig) -> QuickActionsConfig): KamelotProfile =
        updateActiveProfile { profile ->
            profile.copy(quickActionsConfig = normalizeQuickActionsConfig(transform(profile.quickActionsConfig)))
        }

    private fun normalizeProfile(profile: KamelotProfile): KamelotProfile = profile.copy(
        themePreset = ThemePresetMapper.getPreset(profile.themePreset).id,
        appearanceConfig = if (profile.appearanceConfig == KamelotAppearanceConfig()) {
            appearanceConfigForPreset(ThemePresetMapper.getPreset(profile.themePreset))
        } else profile.appearanceConfig,
        quickActionsConfig = normalizeQuickActionsConfig(profile.quickActionsConfig)
    )

    private fun normalizeQuickActionsConfig(config: QuickActionsConfig): QuickActionsConfig {
        val normalizedActions = config.visibleQuickActions
            .sortedBy { it.order }
            .mapIndexed { index, item -> item.copy(order = index) }
        val normalizedMacros = config.visibleMacros
            .sortedBy { it.order }
            .mapIndexed { index, item -> item.copy(order = normalizedActions.size + index) }
        return config.copy(
            maxVisibleItems = config.maxVisibleItems.coerceIn(1, 8),
            visibleQuickActions = normalizedActions,
            visibleMacros = normalizedMacros,
        )
    }

    fun moveQuickAction(actionId: String, direction: Int): KamelotProfile =
        updateActiveQuickActionsConfig { current ->
            current.copy(visibleQuickActions = moveItem(current.visibleQuickActions, direction) { it.id == actionId })
        }

    fun moveMacroStripItem(macroId: String, direction: Int): KamelotProfile =
        updateActiveQuickActionsConfig { current ->
            current.copy(visibleMacros = moveItem(current.visibleMacros, direction) { it.macroId == macroId })
        }

    fun updateActiveGestureActionConfig(transform: (GestureActionConfig) -> GestureActionConfig): KamelotProfile =
        updateActiveProfile { profile ->
            profile.copy(gestureActionConfig = transform(profile.gestureActionConfig))
        }

    private fun <T> moveItem(items: List<T>, direction: Int, predicate: (T) -> Boolean): List<T> {
        val index = items.indexOfFirst(predicate)
        if (index == -1) return items
        val target = (index + direction).coerceIn(0, items.lastIndex)
        if (target == index) return items
        val mutable = items.toMutableList()
        val item = mutable.removeAt(index)
        mutable.add(target, item)
        return mutable
    }

    private fun nextOrder(config: QuickActionsConfig): Int =
        (config.visibleQuickActions + config.visibleMacros).maxOfOrNull { item ->
            when (item) {
                is QuickActionItem -> item.order
                is MacroStripItem -> item.order
                else -> 0
            }
        }?.plus(1) ?: 0

    fun isDefaultProfile(profileId: String): Boolean = profileId in defaultProfileIds()

    private fun defaultProfileIds(): Set<String> = KamelotDefaults.profilePresets.mapTo(linkedSetOf()) { it.id }

    private fun nextProfileName(baseName: String): String {
        val existingNames = loadProfiles().map { it.name }.toSet()
        if (baseName !in existingNames) return baseName
        var index = 2
        while ("$baseName $index" in existingNames) index++
        return "$baseName $index"
    }

    private fun nextProfileId(baseName: String): String {
        val normalized = baseName.lowercase()
            .replace(Regex("[^a-z0-9]+"), "_")
            .trim('_')
            .ifBlank { "profile" }
        val existingIds = loadProfiles().map { it.id }.toSet()
        if (normalized !in existingIds) return normalized
        var index = 2
        while ("${normalized}_$index" in existingIds) index++
        return "${normalized}_$index"
    }
}
