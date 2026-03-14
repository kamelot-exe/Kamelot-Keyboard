// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.NextScreenIcon
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.kamelot.KamelotFeatureFlags
import helium314.keyboard.kamelot.KamelotProfileManager
import helium314.keyboard.kamelot.ThemePresetMapper
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.settings.SettingsActivity
import helium314.keyboard.settings.SettingsDestination
import helium314.keyboard.settings.initPreview

private data class SectionLink(
    val title: String,
    val summary: String,
    val onClick: () -> Unit,
)

@Composable
private fun SettingsSectionScreen(
    onClickBack: () -> Unit,
    title: String,
    subtitle: String,
    links: List<SectionLink>,
    inlineSettings: List<Any?>,
) {
    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = title,
        settings = emptyList(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(text = title, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            links.forEach { link ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = link.onClick),
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(text = link.title, style = MaterialTheme.typography.titleLarge)
                            NextScreenIcon()
                        }
                        Text(
                            text = link.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            inlineSettings.forEach { item ->
                when (item) {
                    is Int -> Text(
                        text = stringResource(item),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                    )
                    null -> Unit
                    else -> SettingsActivity.settingsContainer[item]?.Preference()
                }
            }
        }
    }
}

@Composable
fun AppearanceHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_appearance_title),
        subtitle = stringResource(R.string.settings_section_appearance_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.settings_screen_appearance),
                summary = stringResource(R.string.settings_section_appearance_link_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Appearance) },
            ),
            SectionLink(
                title = stringResource(R.string.kamelot_themes_module_title),
                summary = stringResource(R.string.settings_section_appearance_presets_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotThemes) },
            ),
        ),
        inlineSettings = listOf(
            R.string.settings_section_appearance_group_visuals,
            ThemePresetMapper.PREF_ACTIVE_THEME_PRESET_ID,
            helium314.keyboard.latin.settings.Settings.PREF_THEME_STYLE,
            helium314.keyboard.latin.settings.Settings.PREF_THEME_COLORS,
            helium314.keyboard.latin.settings.Settings.PREF_THEME_KEY_BORDERS,
            R.string.settings_section_appearance_group_size,
            helium314.keyboard.latin.settings.Settings.PREF_FONT_SCALE,
            helium314.keyboard.latin.settings.Settings.PREF_KEYBOARD_HEIGHT_SCALE_PREFIX,
            helium314.keyboard.latin.settings.Settings.PREF_SIDE_PADDING_SCALE_PREFIX,
            "kamelot_theme_presets_foundation",
        ),
    )
}

@Composable
fun LayoutTypingHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_layout_typing_title),
        subtitle = stringResource(R.string.settings_section_layout_typing_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.settings_screen_preferences),
                summary = stringResource(R.string.settings_section_layout_typing_input_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Preferences) },
            ),
            SectionLink(
                title = stringResource(R.string.settings_screen_correction),
                summary = stringResource(R.string.settings_section_layout_typing_correction_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.TextCorrection) },
            ),
            SectionLink(
                title = stringResource(R.string.settings_screen_secondary_layouts),
                summary = stringResource(R.string.settings_section_layout_typing_layouts_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Layouts) },
            ),
        ),
        inlineSettings = listOf(
            R.string.settings_section_layout_typing_group_layout,
            "kamelot_active_layout_mode",
            helium314.keyboard.latin.settings.Settings.PREF_SHOW_NUMBER_ROW,
            helium314.keyboard.latin.settings.Settings.PREF_SHOW_NUMBER_ROW_IN_SYMBOLS,
            helium314.keyboard.latin.settings.Settings.PREF_SHOW_LANGUAGE_SWITCH_KEY,
            R.string.settings_section_layout_typing_group_feedback,
            helium314.keyboard.latin.settings.Settings.PREF_POPUP_ON,
            helium314.keyboard.latin.settings.Settings.PREF_VIBRATE_ON,
            helium314.keyboard.latin.settings.Settings.PREF_SOUND_ON,
        ),
    )
}

@Composable
fun ProfilesHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_profiles_title),
        subtitle = stringResource(R.string.settings_section_profiles_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.kamelot_profiles_module_title),
                summary = stringResource(R.string.settings_section_profiles_link_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotProfiles) },
            ),
            SectionLink(
                title = stringResource(R.string.settings_screen_kamelot),
                summary = stringResource(R.string.settings_section_profiles_dashboard_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Kamelot) },
            ),
        ),
        inlineSettings = listOf(
            KamelotFeatureFlags.PREF_MODULE_PROFILES,
            KamelotProfileManager.PREF_ACTIVE_PROFILE_ID,
            "kamelot_active_layout_mode",
            ThemePresetMapper.PREF_ACTIVE_THEME_PRESET_ID,
            KamelotFeatureFlags.PREF_SHOW_FUTURE_PROFILES_SECTION,
        ),
    )
}

@Composable
fun GesturesHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_gestures_title),
        subtitle = stringResource(R.string.settings_section_gestures_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.settings_screen_gesture),
                summary = stringResource(R.string.settings_section_gestures_typing_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.GestureTyping) },
            ),
            SectionLink(
                title = stringResource(R.string.kamelot_gesture_os_title),
                summary = stringResource(R.string.settings_section_gestures_os_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotGestureOs) },
            ),
        ),
        inlineSettings = listOf(
            KamelotFeatureFlags.PREF_MODULE_GESTURE_OS,
            KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURES,
            KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURE_EXPERIMENTS,
            KamelotFeatureFlags.PREF_ENABLE_PREDICTIVE_HEX_SWIPE_PATH,
        ),
    )
}

@Composable
fun ActionsToolbarHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_actions_toolbar_title),
        subtitle = stringResource(R.string.settings_section_actions_toolbar_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.settings_screen_toolbar),
                summary = stringResource(R.string.settings_section_actions_toolbar_toolbar_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Toolbar) },
            ),
            SectionLink(
                title = stringResource(R.string.kamelot_quick_actions_title),
                summary = stringResource(R.string.settings_section_actions_toolbar_quick_panel_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotQuickActions) },
            ),
        ),
        inlineSettings = listOf(
            KamelotFeatureFlags.PREF_MODULE_QUICK_PANEL,
            KamelotFeatureFlags.PREF_ENABLE_MACRO_STRIP,
            KamelotFeatureFlags.PREF_ENABLE_CUSTOM_ACTIONS,
            "kamelot_action_engine_diagnostics",
        ),
    )
}

@Composable
fun MacrosClipboardHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_macros_clipboard_title),
        subtitle = stringResource(R.string.settings_section_macros_clipboard_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.kamelot_macros_title),
                summary = stringResource(R.string.settings_section_macros_clipboard_macros_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotMacros) },
            ),
            SectionLink(
                title = stringResource(R.string.clipboard),
                summary = stringResource(R.string.settings_section_macros_clipboard_clipboard_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Preferences) },
            ),
        ),
        inlineSettings = listOf(
            KamelotFeatureFlags.PREF_MODULE_MACROS,
            helium314.keyboard.latin.settings.Settings.PREF_ENABLE_CLIPBOARD_HISTORY,
            helium314.keyboard.latin.settings.Settings.PREF_CLIPBOARD_HISTORY_RETENTION_TIME,
            helium314.keyboard.latin.settings.Settings.PREF_CLIPBOARD_HISTORY_PINNED_FIRST,
        ),
    )
}

@Composable
fun LanguagesHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_languages_title),
        subtitle = stringResource(R.string.settings_section_languages_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.language_and_layouts_title),
                summary = stringResource(R.string.settings_section_languages_primary_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Languages) },
            ),
            SectionLink(
                title = stringResource(R.string.settings_screen_secondary_layouts),
                summary = stringResource(R.string.settings_section_languages_layouts_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Layouts) },
            ),
            SectionLink(
                title = stringResource(R.string.dictionary_settings_category),
                summary = stringResource(R.string.settings_section_languages_dictionary_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Dictionaries) },
            ),
        ),
        inlineSettings = listOf(
            helium314.keyboard.latin.settings.Settings.PREF_SAVE_SUBTYPE_PER_APP,
            helium314.keyboard.latin.settings.Settings.PREF_SHOW_LANGUAGE_SWITCH_KEY,
            helium314.keyboard.latin.settings.Settings.PREF_LANGUAGE_SWITCH_KEY,
        ),
    )
}

@Composable
fun PrivacyDataHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_privacy_title),
        subtitle = stringResource(R.string.settings_section_privacy_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.settings_screen_advanced),
                summary = stringResource(R.string.settings_section_privacy_advanced_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Advanced) },
            ),
            SectionLink(
                title = stringResource(R.string.kamelot_modules_title),
                summary = stringResource(R.string.settings_section_privacy_modules_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotModules) },
            ),
        ),
        inlineSettings = listOf(
            helium314.keyboard.latin.settings.Settings.PREF_ENABLE_CLIPBOARD_HISTORY,
            KamelotFeatureFlags.PREF_ENABLE_ADAPTIVE_HEX_HIT_ZONES,
            "kamelot_reset_adaptive_hex_hits",
            helium314.keyboard.latin.settings.Settings.PREF_ALWAYS_INCOGNITO_MODE,
        ),
    )
}

@Composable
fun ExperimentalHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_experimental_title),
        subtitle = stringResource(R.string.settings_section_experimental_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.kamelot_experiments_title),
                summary = stringResource(R.string.settings_section_experimental_lab_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotExperiments) },
            ),
            SectionLink(
                title = stringResource(R.string.kamelot_hex_module_title),
                summary = stringResource(R.string.settings_section_experimental_hex_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotExperiments) },
            ),
        ),
        inlineSettings = listOf(
            KamelotFeatureFlags.PREF_ENABLE_EXPERIMENTAL_FEATURES,
            KamelotFeatureFlags.PREF_ENABLE_HEX_LAYOUT,
            KamelotFeatureFlags.PREF_ENABLE_ADAPTIVE_HEX_HIT_ZONES,
            KamelotFeatureFlags.PREF_ENABLE_PREDICTIVE_HEX_SWIPE_PATH,
        ),
    )
}

@Composable
fun AboutAdvancedHubScreen(onClickBack: () -> Unit) {
    SettingsSectionScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_section_about_advanced_title),
        subtitle = stringResource(R.string.settings_section_about_advanced_summary),
        links = listOf(
            SectionLink(
                title = stringResource(R.string.settings_screen_advanced),
                summary = stringResource(R.string.settings_section_about_advanced_system_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.Advanced) },
            ),
            SectionLink(
                title = stringResource(R.string.settings_screen_about),
                summary = stringResource(R.string.settings_section_about_advanced_about_summary),
                onClick = { SettingsDestination.navigateTo(SettingsDestination.About) },
            ),
        ),
        inlineSettings = emptyList(),
    )
}

@Preview
@Composable
private fun PreviewSection() {
    initPreview(LocalContext.current)
    Theme(previewDark) {
        Surface {
            AppearanceHubScreen { }
        }
    }
}
