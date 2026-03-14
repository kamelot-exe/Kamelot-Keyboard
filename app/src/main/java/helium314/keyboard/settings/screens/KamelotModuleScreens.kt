// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import helium314.keyboard.kamelot.KamelotFeatureFlags
import helium314.keyboard.kamelot.KamelotProfileManager
import helium314.keyboard.kamelot.ThemePresetMapper
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.settings.initPreview

@Composable
fun KamelotProfilesScreen(onClickBack: () -> Unit) {
    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.kamelot_profiles_module_title),
        settings = listOf(
            KamelotProfileManager.PREF_ACTIVE_PROFILE_ID,
            "kamelot_active_layout_mode",
            "kamelot_context_profiles_foundation",
            KamelotFeatureFlags.PREF_SHOW_FUTURE_PROFILES_SECTION,
            KamelotFeatureFlags.PREF_ENABLE_CUSTOM_PROFILES,
        )
    )
}

@Composable
fun KamelotThemesScreen(onClickBack: () -> Unit) {
    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.kamelot_themes_module_title),
        settings = listOf(
            ThemePresetMapper.PREF_ACTIVE_THEME_PRESET_ID,
            "kamelot_theme_presets_foundation",
        )
    )
}

@Composable
fun KamelotModulesScreen(onClickBack: () -> Unit) {
    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.kamelot_modules_title),
        settings = listOf(
            R.string.kamelot_modules_section_title,
            KamelotFeatureFlags.PREF_MODULE_PROFILES,
            KamelotFeatureFlags.PREF_MODULE_THEMES,
            KamelotFeatureFlags.PREF_MODULE_QUICK_PANEL,
            KamelotFeatureFlags.PREF_MODULE_MACROS,
            KamelotFeatureFlags.PREF_MODULE_GESTURE_OS,
            KamelotFeatureFlags.PREF_MODULE_HEX_LAB,
            R.string.kamelot_modules_section_runtime,
            KamelotFeatureFlags.PREF_ENABLE_EXPERIMENTAL_FEATURES,
            KamelotFeatureFlags.PREF_ENABLE_MACRO_STRIP,
            KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURES,
            KamelotFeatureFlags.PREF_ENABLE_HEX_LAYOUT,
        )
    )
}

@Composable
fun KamelotExperimentsScreen(onClickBack: () -> Unit) {
    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.kamelot_experiments_title),
        settings = listOf(
            KamelotFeatureFlags.PREF_ENABLE_EXPERIMENTAL_FEATURES,
            KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURE_EXPERIMENTS,
            KamelotFeatureFlags.PREF_ENABLE_MACRO_STRIP,
            KamelotFeatureFlags.PREF_ENABLE_HEX_LAYOUT,
            KamelotFeatureFlags.PREF_ENABLE_ADAPTIVE_HEX_HIT_ZONES,
            KamelotFeatureFlags.PREF_ENABLE_PREDICTIVE_HEX_SWIPE_PATH,
            "kamelot_reset_adaptive_hex_hits",
            KamelotFeatureFlags.PREF_ENABLE_CUSTOM_ACTIONS,
            KamelotFeatureFlags.PREF_ENABLE_CUSTOM_PROFILES,
            "kamelot_action_engine_diagnostics",
        )
    )
}

@Preview
@Composable
private fun PreviewProfiles() {
    initPreview(LocalContext.current)
    Theme(previewDark) {
        Surface {
            KamelotProfilesScreen { }
        }
    }
}
