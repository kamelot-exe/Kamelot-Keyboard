// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import android.content.Context
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import helium314.keyboard.kamelot.KamelotConfig
import helium314.keyboard.kamelot.KamelotDefaults
import helium314.keyboard.kamelot.KamelotFeatureFlags
import helium314.keyboard.kamelot.KamelotProfileManager
import helium314.keyboard.kamelot.ThemePresetMapper
import helium314.keyboard.kamelot.actions.KamelotActionContext
import helium314.keyboard.kamelot.actions.KamelotActionDispatcher
import helium314.keyboard.kamelot.actions.KamelotActionResult
import helium314.keyboard.keyboard.KeyboardSwitcher
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.settings.Setting
import helium314.keyboard.settings.preferences.ListPreference
import helium314.keyboard.settings.preferences.Preference
import helium314.keyboard.settings.preferences.SwitchPreference
import helium314.keyboard.settings.initPreview

@Composable
fun KamelotScreen(onClickBack: () -> Unit) {
    val items = listOf(
        R.string.kamelot_category_configuration,
        KamelotProfileManager.PREF_ACTIVE_PROFILE_ID,
        ThemePresetMapper.PREF_ACTIVE_THEME_PRESET_ID,
        R.string.kamelot_category_foundation,
        "kamelot_theme_presets_foundation",
        "kamelot_profiles_foundation",
        "kamelot_layout_modes_foundation",
        "kamelot_action_definitions_foundation",
        "kamelot_action_engine_diagnostics",
        R.string.kamelot_category_flags,
        KamelotFeatureFlags.PREF_ENABLE_EXPERIMENTAL_FEATURES,
        KamelotFeatureFlags.PREF_SHOW_FUTURE_PROFILES_SECTION,
        KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURE_EXPERIMENTS,
    )
    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_screen_kamelot),
        settings = items
    )
}

fun createKamelotSettings(context: Context) = listOf(
    Setting(
        context,
        KamelotProfileManager.PREF_ACTIVE_PROFILE_ID,
        R.string.kamelot_active_profile_title,
        R.string.kamelot_active_profile_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = manager.loadProfiles().map { profile -> profile.name to profile.id }
        ListPreference(setting, items, manager.getActiveProfile().id) {
            manager.switchProfile(it)
            KeyboardSwitcher.getInstance().setThemeNeedsReload()
        }
    },
    Setting(
        context,
        ThemePresetMapper.PREF_ACTIVE_THEME_PRESET_ID,
        R.string.kamelot_theme_preset_selector_title,
        R.string.kamelot_theme_preset_selector_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = ThemePresetMapper.presets.map { preset -> preset.displayName to preset.id }
        ListPreference(setting, items, manager.getActiveProfile().themePreset) {
            manager.updateActiveProfileThemePreset(it)
            KeyboardSwitcher.getInstance().setThemeNeedsReload()
        }
    },
    Setting(context, "kamelot_theme_presets_foundation", R.string.kamelot_theme_presets_title) {
        val presets = KamelotDefaults.themePresets.joinToString(", ") { preset -> preset.displayName }
        Preference(
            name = it.title,
            description = "$presets. ${context.getString(R.string.kamelot_theme_presets_summary)}",
            onClick = {}
        )
    },
    Setting(context, "kamelot_profiles_foundation", R.string.kamelot_profiles_title) {
        val profiles = KamelotDefaults.profilePresets.joinToString(", ") { profile -> profile.name }
        Preference(
            name = it.title,
            description = "$profiles. ${context.getString(R.string.kamelot_profiles_summary)}",
            onClick = {}
        )
    },
    Setting(context, "kamelot_layout_modes_foundation", R.string.kamelot_layout_modes_title, R.string.kamelot_layout_modes_summary) {
        val modes = KamelotConfig.supportedLayoutModes.joinToString(", ") { mode -> mode.name.lowercase() }
        Preference(
            name = it.title,
            description = "${it.description} Supported placeholders: $modes",
            onClick = {}
        )
    },
    Setting(context, "kamelot_action_definitions_foundation", R.string.kamelot_action_definitions_title, R.string.kamelot_action_definitions_summary) {
        Preference(
            name = it.title,
            description = it.description,
            onClick = {}
        )
    },
    Setting(context, "kamelot_action_engine_diagnostics", R.string.kamelot_action_engine_diagnostics_title) {
        val ctx = LocalContext.current
        val manager = KamelotProfileManager(ctx)
        val profile = manager.getActiveProfile()
        val macros = manager.getMacroMap(profile)
        val dispatcher = KamelotActionDispatcher()
        val dryRunContext = KamelotActionContext(
            context = ctx,
            profileManager = manager,
            macros = macros,
            dryRun = true,
        )
        val summary = dispatcher.profileActions(profile).joinToString(" | ") { action ->
            val result = dispatcher.dispatch(action, dryRunContext)
            val resultLabel = when (result) {
                is KamelotActionResult.Success -> "success"
                is KamelotActionResult.Ignored -> "ignored"
                is KamelotActionResult.Unsupported -> "unsupported"
                is KamelotActionResult.FailedSafely -> "failed"
            }
            "${action.type.name}:$resultLabel"
        }.ifEmpty { context.getString(R.string.kamelot_action_engine_diagnostics_summary) }
        Preference(
            name = it.title,
            description = "Active profile ${profile.name}: $summary",
            onClick = {}
        )
    },
    Setting(
        context,
        KamelotFeatureFlags.PREF_ENABLE_EXPERIMENTAL_FEATURES,
        R.string.kamelot_enable_experimental_features,
        R.string.kamelot_enable_experimental_features_summary
    ) {
        SwitchPreference(it, KamelotDefaults.ENABLE_EXPERIMENTAL_FEATURES)
    },
    Setting(
        context,
        KamelotFeatureFlags.PREF_SHOW_FUTURE_PROFILES_SECTION,
        R.string.kamelot_show_future_profiles_section,
        R.string.kamelot_show_future_profiles_section_summary
    ) {
        SwitchPreference(it, KamelotDefaults.SHOW_FUTURE_PROFILES_SECTION)
    },
    Setting(
        context,
        KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURE_EXPERIMENTS,
        R.string.kamelot_enable_advanced_gesture_experiments,
        R.string.kamelot_enable_advanced_gesture_experiments_summary
    ) {
        SwitchPreference(it, KamelotDefaults.ENABLE_ADVANCED_GESTURE_EXPERIMENTS)
    },
)

@Preview
@Composable
private fun Preview() {
    initPreview(LocalContext.current)
    Theme(previewDark) {
        Surface {
            KamelotScreen { }
        }
    }
}
