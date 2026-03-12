// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import helium314.keyboard.latin.utils.prefs
import kotlinx.serialization.json.Json

class KamelotProfileManager(
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
            if (profile.id == current.id) profile.copy(themePreset = ThemePresetMapper.getPreset(themePresetId).id)
            else profile
        }
        saveProfiles(updatedProfiles)
        val updated = updatedProfiles.first { it.id == current.id }
        prefs.edit { putString(PREF_ACTIVE_PROFILE_ID, updated.id) }
        ThemePresetMapper.applyPreset(context, updated.themePreset, prefs)
        return updated
    }
}
