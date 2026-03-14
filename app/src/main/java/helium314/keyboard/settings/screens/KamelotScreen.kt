// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import helium314.keyboard.kamelot.KamelotConfig
import helium314.keyboard.kamelot.KamelotDefaults
import helium314.keyboard.kamelot.KamelotFeatureFlags
import helium314.keyboard.kamelot.KamelotProfileManager
import helium314.keyboard.kamelot.KamelotAccentPalette
import helium314.keyboard.kamelot.KamelotBorderStyle
import helium314.keyboard.kamelot.KamelotCornerStyle
import helium314.keyboard.kamelot.KamelotGlowIntensity
import helium314.keyboard.kamelot.KamelotKeyBackgroundStyle
import helium314.keyboard.kamelot.KamelotSpacingDensity
import helium314.keyboard.kamelot.ThemePresetMapper
import helium314.keyboard.kamelot.actions.KamelotActionContext
import helium314.keyboard.kamelot.actions.KamelotActionDispatcher
import helium314.keyboard.kamelot.actions.KamelotActionResult
import helium314.keyboard.kamelot.displayName
import helium314.keyboard.kamelot.layout.AdaptiveHitModel
import helium314.keyboard.keyboard.KeyboardSwitcher
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.NextScreenIcon
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.getActivity
import helium314.keyboard.latin.utils.prefs
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.settings.Setting
import helium314.keyboard.settings.SettingsActivity
import helium314.keyboard.settings.SettingsDestination
import helium314.keyboard.settings.initPreview
import helium314.keyboard.settings.preferences.ListPreference
import helium314.keyboard.settings.preferences.Preference
import helium314.keyboard.settings.preferences.SwitchPreference

private fun enumLabel(value: String) = value.lowercase().replace('_', ' ').split(' ')
    .joinToString(" ") { part -> part.replaceFirstChar { char -> char.uppercase() } }

private data class KamelotModuleCard(
    val title: String,
    val summary: String,
    val enabled: Boolean,
    val badge: String,
    val onClick: () -> Unit,
)

@Composable
fun KamelotScreen(onClickBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = context.prefs()
    (context.getActivity() as? SettingsActivity)?.prefChanged?.collectAsState()
    val manager = KamelotProfileManager(context)
    val activeProfile = manager.getActiveProfile()
    val moduleCards = listOf(
        KamelotModuleCard(
            title = stringResource(R.string.kamelot_profiles_module_title),
            summary = stringResource(R.string.kamelot_profiles_module_summary, activeProfile.name),
            enabled = KamelotFeatureFlags.isModuleEnabled(prefs, KamelotFeatureFlags.PREF_MODULE_PROFILES),
            badge = activeProfile.name,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotProfiles) },
        ),
        KamelotModuleCard(
            title = stringResource(R.string.kamelot_themes_module_title),
            summary = stringResource(
                R.string.kamelot_themes_module_summary,
                ThemePresetMapper.getPreset(activeProfile.themePreset).displayName
            ),
            enabled = KamelotFeatureFlags.isModuleEnabled(prefs, KamelotFeatureFlags.PREF_MODULE_THEMES),
            badge = ThemePresetMapper.getPreset(activeProfile.themePreset).displayName,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotThemes) },
        ),
        KamelotModuleCard(
            title = stringResource(R.string.kamelot_quick_actions_title),
            summary = stringResource(R.string.kamelot_quick_panel_module_summary),
            enabled = KamelotFeatureFlags.isModuleEnabled(prefs, KamelotFeatureFlags.PREF_MODULE_QUICK_PANEL),
            badge = activeProfile.quickActionsConfig.quickPanelPreset.displayName,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotQuickActions) },
        ),
        KamelotModuleCard(
            title = stringResource(R.string.kamelot_macros_title),
            summary = stringResource(R.string.kamelot_macros_module_summary, activeProfile.macros.size),
            enabled = KamelotFeatureFlags.isModuleEnabled(prefs, KamelotFeatureFlags.PREF_MODULE_MACROS),
            badge = activeProfile.macros.size.toString(),
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotMacros) },
        ),
        KamelotModuleCard(
            title = stringResource(R.string.kamelot_gesture_os_title),
            summary = stringResource(R.string.kamelot_gesture_module_summary),
            enabled = KamelotFeatureFlags.isModuleEnabled(prefs, KamelotFeatureFlags.PREF_MODULE_GESTURE_OS),
            badge = activeProfile.gestureActionConfig.preset.displayName,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotGestureOs) },
        ),
        KamelotModuleCard(
            title = stringResource(R.string.kamelot_hex_module_title),
            summary = stringResource(R.string.kamelot_hex_module_summary),
            enabled = KamelotFeatureFlags.isModuleEnabled(prefs, KamelotFeatureFlags.PREF_MODULE_HEX_LAB),
            badge = activeProfile.layoutMode.displayName,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotExperiments) },
        ),
    )

    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.settings_screen_kamelot),
        settings = emptyList(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            KamelotHeroCard(
                title = stringResource(R.string.kamelot_dashboard_title),
                subtitle = stringResource(
                    R.string.kamelot_dashboard_summary,
                    activeProfile.name,
                    ThemePresetMapper.getPreset(activeProfile.themePreset).displayName
                ),
            )
            KamelotUtilityRow(
                primaryLabel = stringResource(R.string.kamelot_modules_title),
                primarySummary = stringResource(R.string.kamelot_modules_summary),
                onPrimaryClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotModules) },
                secondaryLabel = stringResource(R.string.kamelot_experiments_title),
                secondarySummary = stringResource(R.string.kamelot_experiments_summary),
                onSecondaryClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotExperiments) },
            )
            Text(
                text = stringResource(R.string.kamelot_modules_section_title),
                style = MaterialTheme.typography.titleMedium,
            )
            moduleCards.forEach { card ->
                KamelotModuleCardView(card)
            }
            KamelotInfoCard(
                title = stringResource(R.string.kamelot_global_search_title),
                summary = stringResource(R.string.kamelot_global_search_summary),
            )
        }
    }
}

@Composable
private fun KamelotHeroCard(title: String, subtitle: String) {
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
}

@Composable
private fun KamelotUtilityRow(
    primaryLabel: String,
    primarySummary: String,
    onPrimaryClick: () -> Unit,
    secondaryLabel: String,
    secondarySummary: String,
    onSecondaryClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        KamelotUtilityCard(
            title = primaryLabel,
            summary = primarySummary,
            onClick = onPrimaryClick,
        )
        KamelotUtilityCard(
            title = secondaryLabel,
            summary = secondarySummary,
            onClick = onSecondaryClick,
        )
    }
}

@Composable
private fun KamelotUtilityCard(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun KamelotModuleCardView(card: KamelotModuleCard) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = card.onClick),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = card.title, style = MaterialTheme.typography.titleLarge)
                NextScreenIcon()
            }
            Text(
                text = card.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                KamelotBadge(
                    text = if (card.enabled) stringResource(R.string.kamelot_module_enabled) else stringResource(R.string.kamelot_module_disabled),
                    emphasized = card.enabled,
                )
                KamelotBadge(text = card.badge, emphasized = false)
            }
        }
    }
}

@Composable
private fun KamelotBadge(text: String, emphasized: Boolean) {
    AssistChip(
        onClick = {},
        enabled = false,
        label = { Text(text) },
        colors = AssistChipDefaults.assistChipColors(
            disabledContainerColor = if (emphasized) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            disabledLabelColor = if (emphasized) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        border = null,
    )
}

@Composable
private fun KamelotInfoCard(title: String, summary: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(24.dp),
            )
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

fun createKamelotSettings(context: Context) = listOf(
    Setting(context, "kamelot_profiles_screen", R.string.kamelot_profiles_module_title, R.string.kamelot_profiles_module_summary_search) {
        Preference(
            name = it.title,
            description = it.description,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotProfiles) },
        ) { NextScreenIcon() }
    },
    Setting(context, "kamelot_themes_screen", R.string.kamelot_themes_module_title, R.string.kamelot_themes_module_summary_search) {
        Preference(
            name = it.title,
            description = it.description,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotThemes) },
        ) { NextScreenIcon() }
    },
    Setting(context, "kamelot_modules_screen", R.string.kamelot_modules_title, R.string.kamelot_modules_summary) {
        Preference(
            name = it.title,
            description = it.description,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotModules) },
        ) { NextScreenIcon() }
    },
    Setting(context, "kamelot_experiments_screen", R.string.kamelot_experiments_title, R.string.kamelot_experiments_summary) {
        Preference(
            name = it.title,
            description = it.description,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotExperiments) },
        ) { NextScreenIcon() }
    },
    Setting(
        context,
        KamelotFeatureFlags.PREF_MODULE_PROFILES,
        R.string.kamelot_module_profiles_title,
        R.string.kamelot_module_profiles_summary
    ) { SwitchPreference(it, true) },
    Setting(
        context,
        KamelotFeatureFlags.PREF_MODULE_THEMES,
        R.string.kamelot_module_themes_title,
        R.string.kamelot_module_themes_summary
    ) { SwitchPreference(it, true) },
    Setting(
        context,
        KamelotFeatureFlags.PREF_MODULE_QUICK_PANEL,
        R.string.kamelot_module_quick_panel_title,
        R.string.kamelot_module_quick_panel_summary
    ) { SwitchPreference(it, true) { KeyboardSwitcher.getInstance().setThemeNeedsReload() } },
    Setting(
        context,
        KamelotFeatureFlags.PREF_MODULE_MACROS,
        R.string.kamelot_module_macros_title,
        R.string.kamelot_module_macros_summary
    ) { SwitchPreference(it, true) },
    Setting(
        context,
        KamelotFeatureFlags.PREF_MODULE_GESTURE_OS,
        R.string.kamelot_module_gesture_os_title,
        R.string.kamelot_module_gesture_os_summary
    ) { SwitchPreference(it, true) { KeyboardSwitcher.getInstance().setThemeNeedsReload() } },
    Setting(
        context,
        KamelotFeatureFlags.PREF_MODULE_HEX_LAB,
        R.string.kamelot_module_hex_title,
        R.string.kamelot_module_hex_summary
    ) { SwitchPreference(it, true) { KeyboardSwitcher.getInstance().setThemeNeedsReload() } },
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
    Setting(
        context,
        "kamelot_appearance_accent_palette",
        R.string.kamelot_appearance_accent_palette_title,
        R.string.kamelot_appearance_accent_palette_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = KamelotAccentPalette.entries.map { enumLabel(it.name) to it.name }
        ListPreference(setting, items, manager.getActiveProfile().appearanceConfig.accentPalette.name) {
            manager.updateActiveAppearanceConfig { config ->
                config.copy(accentPalette = KamelotAccentPalette.valueOf(it))
            }
        }
    },
    Setting(
        context,
        "kamelot_appearance_key_background_style",
        R.string.kamelot_appearance_key_background_title,
        R.string.kamelot_appearance_key_background_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = KamelotKeyBackgroundStyle.entries.map { enumLabel(it.name) to it.name }
        ListPreference(setting, items, manager.getActiveProfile().appearanceConfig.keyBackgroundStyle.name) {
            manager.updateActiveAppearanceConfig { config ->
                config.copy(keyBackgroundStyle = KamelotKeyBackgroundStyle.valueOf(it))
            }
        }
    },
    Setting(
        context,
        "kamelot_appearance_border_style",
        R.string.kamelot_appearance_border_style_title,
        R.string.kamelot_appearance_border_style_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = KamelotBorderStyle.entries.map { enumLabel(it.name) to it.name }
        ListPreference(setting, items, manager.getActiveProfile().appearanceConfig.borderStyle.name) {
            manager.updateActiveAppearanceConfig { config ->
                config.copy(borderStyle = KamelotBorderStyle.valueOf(it))
            }
        }
    },
    Setting(
        context,
        "kamelot_appearance_corner_style",
        R.string.kamelot_appearance_corner_style_title,
        R.string.kamelot_appearance_corner_style_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = KamelotCornerStyle.entries.map { enumLabel(it.name) to it.name }
        ListPreference(setting, items, manager.getActiveProfile().appearanceConfig.cornerStyle.name) {
            manager.updateActiveAppearanceConfig { config ->
                config.copy(cornerStyle = KamelotCornerStyle.valueOf(it))
            }
        }
    },
    Setting(
        context,
        "kamelot_appearance_glow_intensity",
        R.string.kamelot_appearance_glow_intensity_title,
        R.string.kamelot_appearance_glow_intensity_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = KamelotGlowIntensity.entries.map { enumLabel(it.name) to it.name }
        ListPreference(setting, items, manager.getActiveProfile().appearanceConfig.glowIntensity.name) {
            manager.updateActiveAppearanceConfig { config ->
                config.copy(glowIntensity = KamelotGlowIntensity.valueOf(it))
            }
        }
    },
    Setting(
        context,
        "kamelot_appearance_spacing_density",
        R.string.kamelot_appearance_spacing_density_title,
        R.string.kamelot_appearance_spacing_density_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = KamelotSpacingDensity.entries.map { enumLabel(it.name) to it.name }
        ListPreference(setting, items, manager.getActiveProfile().appearanceConfig.spacingDensity.name) {
            manager.updateActiveAppearanceConfig { config ->
                config.copy(spacingDensity = KamelotSpacingDensity.valueOf(it))
            }
        }
    },
    Setting(
        context,
        "kamelot_active_layout_mode",
        R.string.kamelot_layout_mode_selector_title,
        R.string.kamelot_layout_mode_selector_summary
    ) { setting ->
        val manager = KamelotProfileManager(LocalContext.current)
        val items = KamelotConfig.supportedLayoutModes.map { mode -> mode.displayName to mode.name }
        ListPreference(setting, items, manager.getActiveProfile().layoutMode.name) {
            manager.updateActiveProfileLayoutMode(KamelotConfig.supportedLayoutModes.first { mode -> mode.name == it })
            KeyboardSwitcher.getInstance().setThemeNeedsReload()
        }
    },
    Setting(context, "kamelot_gesture_os_screen", R.string.kamelot_gesture_os_title, R.string.kamelot_gesture_os_summary) {
        Preference(
            name = it.title,
            description = it.description,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotGestureOs) }
        ) { NextScreenIcon() }
    },
    Setting(context, "kamelot_quick_actions_screen", R.string.kamelot_quick_actions_title, R.string.kamelot_quick_actions_summary) {
        Preference(
            name = it.title,
            description = it.description,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotQuickActions) }
        ) { NextScreenIcon() }
    },
    Setting(context, "kamelot_macro_manager_screen", R.string.kamelot_macros_title, R.string.kamelot_macros_summary) {
        Preference(
            name = it.title,
            description = it.description,
            onClick = { SettingsDestination.navigateTo(SettingsDestination.KamelotMacros) }
        ) { NextScreenIcon() }
    },
    Setting(context, "kamelot_theme_presets_foundation", R.string.kamelot_theme_presets_title) {
        val presets = KamelotDefaults.themePresets.joinToString(", ") { preset -> preset.displayName }
        Preference(name = it.title, description = "$presets. ${context.getString(R.string.kamelot_theme_presets_summary)}", onClick = {})
    },
    Setting(context, "kamelot_profiles_foundation", R.string.kamelot_profiles_title) {
        val profiles = KamelotDefaults.profilePresets.joinToString(", ") { profile -> profile.name }
        Preference(name = it.title, description = "$profiles. ${context.getString(R.string.kamelot_profiles_summary)}", onClick = {})
    },
    Setting(context, "kamelot_layout_modes_foundation", R.string.kamelot_layout_modes_title, R.string.kamelot_layout_modes_summary) {
        val modes = KamelotConfig.supportedLayoutModes.joinToString(", ") { mode -> mode.name.lowercase() }
        Preference(name = it.title, description = "${it.description} Supported modes: $modes", onClick = {})
    },
    Setting(context, "kamelot_action_definitions_foundation", R.string.kamelot_action_definitions_title, R.string.kamelot_action_definitions_summary) {
        Preference(name = it.title, description = it.description, onClick = {})
    },
    Setting(context, "kamelot_context_profiles_foundation", R.string.kamelot_context_profiles_title, R.string.kamelot_context_profiles_summary) {
        Preference(name = it.title, description = it.description, onClick = {})
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
        val summary = dispatcher.profileActions(profile, dryRunContext).joinToString(" | ") { action ->
            val result = dispatcher.dispatch(action, dryRunContext)
            val resultLabel = when (result) {
                is KamelotActionResult.Success -> "success"
                is KamelotActionResult.Ignored -> "ignored"
                is KamelotActionResult.Unsupported -> "unsupported"
                is KamelotActionResult.FailedSafely -> "failed"
            }
            "${action.type.name}:$resultLabel"
        }.ifEmpty { context.getString(R.string.kamelot_action_engine_diagnostics_summary) }
        Preference(name = it.title, description = "Active profile ${profile.name}: $summary", onClick = {})
    },
    Setting(
        context,
        KamelotFeatureFlags.PREF_ENABLE_EXPERIMENTAL_FEATURES,
        R.string.kamelot_enable_experimental_features,
        R.string.kamelot_enable_experimental_features_summary
    ) { SwitchPreference(it, KamelotDefaults.ENABLE_EXPERIMENTAL_FEATURES) },
    Setting(
        context,
        KamelotFeatureFlags.PREF_SHOW_FUTURE_PROFILES_SECTION,
        R.string.kamelot_show_future_profiles_section,
        R.string.kamelot_show_future_profiles_section_summary
    ) { SwitchPreference(it, KamelotDefaults.SHOW_FUTURE_PROFILES_SECTION) },
    Setting(
        context,
        KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURE_EXPERIMENTS,
        R.string.kamelot_enable_advanced_gesture_experiments,
        R.string.kamelot_enable_advanced_gesture_experiments_summary
    ) { SwitchPreference(it, KamelotDefaults.ENABLE_ADVANCED_GESTURE_EXPERIMENTS) },
    Setting(context, KamelotFeatureFlags.PREF_ENABLE_MACRO_STRIP, R.string.kamelot_enable_macro_strip, R.string.kamelot_enable_macro_strip_summary) {
        SwitchPreference(it, false) { KeyboardSwitcher.getInstance().setThemeNeedsReload() }
    },
    Setting(context, KamelotFeatureFlags.PREF_ENABLE_HEX_LAYOUT, R.string.kamelot_enable_hex_layout, R.string.kamelot_enable_hex_layout_summary) {
        SwitchPreference(it, false) { KeyboardSwitcher.getInstance().setThemeNeedsReload() }
    },
    Setting(context, KamelotFeatureFlags.PREF_ENABLE_ADAPTIVE_HEX_HIT_ZONES, R.string.kamelot_enable_adaptive_hex_hit_zones, R.string.kamelot_enable_adaptive_hex_hit_zones_summary) {
        SwitchPreference(it, false) { KeyboardSwitcher.getInstance().setThemeNeedsReload() }
    },
    Setting(context, KamelotFeatureFlags.PREF_ENABLE_PREDICTIVE_HEX_SWIPE_PATH, R.string.kamelot_enable_predictive_hex_swipe_path, R.string.kamelot_enable_predictive_hex_swipe_path_summary) {
        SwitchPreference(it, false)
    },
    Setting(context, "kamelot_reset_adaptive_hex_hits", R.string.kamelot_reset_adaptive_hex_hits_title, R.string.kamelot_reset_adaptive_hex_hits_summary) {
        val currentContext = LocalContext.current
        Preference(name = it.title, description = it.description, onClick = { AdaptiveHitModel(currentContext).reset() })
    },
    Setting(context, KamelotFeatureFlags.PREF_ENABLE_CUSTOM_ACTIONS, R.string.kamelot_enable_custom_actions, R.string.kamelot_enable_custom_actions_summary) {
        SwitchPreference(it, false)
    },
    Setting(context, KamelotFeatureFlags.PREF_ENABLE_CUSTOM_PROFILES, R.string.kamelot_enable_custom_profiles, R.string.kamelot_enable_custom_profiles_summary) {
        SwitchPreference(it, false)
    },
    Setting(context, KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURES, R.string.kamelot_enable_gesture_os, R.string.kamelot_enable_gesture_os_summary) {
        SwitchPreference(it, false) { KeyboardSwitcher.getInstance().setThemeNeedsReload() }
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
