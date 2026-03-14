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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import helium314.keyboard.kamelot.KamelotFeatureFlags
import helium314.keyboard.kamelot.KamelotProfileManager
import helium314.keyboard.kamelot.ThemePresetMapper
import helium314.keyboard.keyboard.KeyboardSwitcher
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.getActivity
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.settings.SettingsActivity
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
    val context = LocalContext.current
    val manager = KamelotProfileManager(context)
    (context.getActivity() as? SettingsActivity)?.prefChanged?.collectAsState()
    val activeProfile = manager.getActiveProfile()
    val activePreset = ThemePresetMapper.getPreset(activeProfile.themePreset)
    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.kamelot_themes_module_title),
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
                    Text(
                        text = stringResource(R.string.kamelot_themes_module_title),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = stringResource(R.string.kamelot_theme_studio_summary, activePreset.displayName),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            ThemePresetMapper.presets.forEach { preset ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            manager.updateActiveProfileThemePreset(preset.id)
                            KeyboardSwitcher.getInstance().setThemeNeedsReload()
                        },
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
                            Text(
                                text = preset.displayName,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            if (preset.id == activePreset.id) {
                                AssistChip(
                                    onClick = {},
                                    enabled = false,
                                    label = { Text(stringResource(R.string.kamelot_active_badge)) },
                                )
                            }
                        }
                        Text(
                            text = preset.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AssistChip(onClick = {}, enabled = false, label = {
                                Text(stringResource(R.string.kamelot_theme_chip_style, preset.keyShape.name.lowercase()))
                            })
                            AssistChip(onClick = {}, enabled = false, label = {
                                Text(stringResource(R.string.kamelot_theme_chip_background, preset.backgroundStyle.name.lowercase()))
                            })
                            AssistChip(onClick = {}, enabled = false, label = {
                                Text(stringResource(R.string.kamelot_theme_chip_glow, preset.glowIntensity.name.lowercase()))
                            })
                        }
                    }
                }
            }
            listOf(
                ThemePresetMapper.PREF_ACTIVE_THEME_PRESET_ID,
                "kamelot_appearance_accent_palette",
                "kamelot_appearance_key_background_style",
                "kamelot_appearance_border_style",
                "kamelot_appearance_corner_style",
                "kamelot_appearance_glow_intensity",
                "kamelot_appearance_spacing_density",
                "kamelot_theme_presets_foundation",
            ).forEach { key ->
                helium314.keyboard.settings.SettingsActivity.settingsContainer[key]?.Preference()
            }
        }
    }
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
