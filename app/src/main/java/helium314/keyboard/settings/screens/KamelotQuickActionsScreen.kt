// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import helium314.keyboard.kamelot.KamelotDefaults
import helium314.keyboard.kamelot.KamelotQuickPanelPreset
import helium314.keyboard.kamelot.KamelotProfileManager
import helium314.keyboard.kamelot.KamelotQuickActionsResolver
import helium314.keyboard.kamelot.KamelotStripBehavior
import helium314.keyboard.kamelot.KamelotAppCategoryIntent
import helium314.keyboard.kamelot.displayName
import helium314.keyboard.keyboard.KeyboardSwitcher
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.Log
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.getActivity
import helium314.keyboard.latin.utils.prefs
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.settings.SettingsActivity
import helium314.keyboard.settings.dialogs.ListPickerDialog
import helium314.keyboard.settings.initPreview
import helium314.keyboard.settings.preferences.Preference
import helium314.keyboard.settings.preferences.PreferenceCategory

@Composable
fun KamelotQuickActionsScreen(onClickBack: () -> Unit) {
    val context = LocalContext.current
    val manager = remember(context) { KamelotProfileManager(context) }
    val prefChanges = (context.getActivity() as? SettingsActivity)?.prefChanged?.collectAsState()
    if ((prefChanges?.value ?: 0) < 0) Log.v("kamelot", "pref recompose")
    val profiles = manager.loadProfiles()
    val activeProfile = manager.getActiveProfile()
    val config = activeProfile.quickActionsConfig
    val resolvedItems = KamelotQuickActionsResolver.resolve(activeProfile, context.prefs(), profiles)
    var pickMode by remember { mutableStateOf(false) }
    var pickPreset by remember { mutableStateOf(false) }
    var pickPanelPreset by remember { mutableStateOf(false) }
    var pickBehavior by remember { mutableStateOf(false) }
    var pickRoutingIntent by remember { mutableStateOf(false) }
    var pickMaxVisible by remember { mutableStateOf(false) }

    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.kamelot_quick_actions_title),
        settings = emptyList(),
        content = {
            PreferenceCategory(stringResource(R.string.kamelot_category_configuration))
            Preference(
                name = stringResource(R.string.kamelot_active_profile_title),
                description = activeProfile.name,
                onClick = {},
            )
            Preference(
                name = stringResource(R.string.kamelot_strip_mode_title),
                description = config.stripMode.displayName,
                onClick = { pickMode = true },
            )
            Preference(
                name = stringResource(R.string.kamelot_strip_preset_title),
                description = config.stripPreset.displayName,
                onClick = { pickPreset = true },
            )
            Preference(
                name = stringResource(R.string.kamelot_quick_panel_preset_title),
                description = config.quickPanelPreset.displayName,
                onClick = { pickPanelPreset = true },
            )
            Preference(
                name = stringResource(R.string.kamelot_strip_behavior_title),
                description = config.stripBehavior.displayName,
                onClick = { pickBehavior = true },
            )
            Preference(
                name = stringResource(R.string.kamelot_context_profiles_title),
                description = activeProfile.routingIntent.appCategoryIntent?.name?.lowercase()
                    ?: stringResource(R.string.action_none),
                onClick = { pickRoutingIntent = true },
            )
            Preference(
                name = stringResource(R.string.kamelot_context_profile_hints_title),
                description = activeProfile.contextHints.joinToString(", ") { it.name.lowercase() },
                onClick = { pickRoutingIntent = true },
            )
            Preference(
                name = stringResource(R.string.kamelot_quick_panel_item_limit_title),
                description = config.maxVisibleItems.toString(),
                onClick = { pickMaxVisible = true },
            )
            Preference(
                name = stringResource(R.string.kamelot_macro_strip_enabled_title),
                description = stringResource(R.string.kamelot_macro_strip_enabled_summary),
                onClick = {
                    manager.updateActiveQuickActionsConfig { it.copy(macroStripEnabled = !config.macroStripEnabled) }
                    KeyboardSwitcher.getInstance().setThemeNeedsReload()
                },
            ) {
                Switch(
                    checked = config.macroStripEnabled,
                    onCheckedChange = {
                        manager.updateActiveQuickActionsConfig { current -> current.copy(macroStripEnabled = it) }
                        KeyboardSwitcher.getInstance().setThemeNeedsReload()
                    }
                )
            }
            Preference(
                name = stringResource(R.string.kamelot_profile_switcher_title),
                description = stringResource(R.string.kamelot_profile_switcher_summary),
                onClick = {
                    manager.updateActiveQuickActionsConfig { it.copy(showProfileSwitcher = !config.showProfileSwitcher) }
                    KeyboardSwitcher.getInstance().setThemeNeedsReload()
                },
            ) {
                Switch(
                    checked = config.showProfileSwitcher,
                    onCheckedChange = {
                        manager.updateActiveQuickActionsConfig { current -> current.copy(showProfileSwitcher = it) }
                        KeyboardSwitcher.getInstance().setThemeNeedsReload()
                    }
                )
            }
            Preference(
                name = stringResource(R.string.kamelot_profile_badge_title),
                description = stringResource(R.string.kamelot_profile_badge_summary),
                onClick = {
                    manager.updateActiveQuickActionsConfig { it.copy(showProfileBadge = !config.showProfileBadge) }
                    KeyboardSwitcher.getInstance().setThemeNeedsReload()
                },
            ) {
                Switch(
                    checked = config.showProfileBadge,
                    onCheckedChange = {
                        manager.updateActiveQuickActionsConfig { current -> current.copy(showProfileBadge = it) }
                        KeyboardSwitcher.getInstance().setThemeNeedsReload()
                    }
                )
            }

            PreferenceCategory(stringResource(R.string.kamelot_quick_actions_section_title))
            if (config.visibleQuickActions.isEmpty()) {
                Preference(
                    name = stringResource(R.string.kamelot_quick_actions_empty_title),
                    description = stringResource(R.string.kamelot_quick_actions_empty_summary),
                    onClick = {},
                )
            } else {
                config.visibleQuickActions.sortedBy { it.order }.forEach { item ->
                    val toggle: (Boolean) -> Unit = { enabled ->
                        manager.updateActiveQuickActionsConfig { current ->
                            current.copy(
                                visibleQuickActions = current.visibleQuickActions.map { existing ->
                                    if (existing.id == item.id) existing.copy(enabled = enabled) else existing
                                }
                            )
                        }
                        KeyboardSwitcher.getInstance().setThemeNeedsReload()
                    }
                    Preference(
                        name = item.label,
                        description = item.action.type.name.replace('_', ' ').lowercase(),
                        onClick = { toggle(!item.enabled) },
                    ) {
                        TextButton(onClick = {
                            manager.moveQuickAction(item.id, -1)
                            KeyboardSwitcher.getInstance().setThemeNeedsReload()
                        }) { Text(stringResource(R.string.kamelot_move_up_short)) }
                        TextButton(onClick = {
                            manager.moveQuickAction(item.id, 1)
                            KeyboardSwitcher.getInstance().setThemeNeedsReload()
                        }) { Text(stringResource(R.string.kamelot_move_down_short)) }
                        Switch(checked = item.enabled, onCheckedChange = toggle)
                    }
                }
            }

            PreferenceCategory(stringResource(R.string.kamelot_macro_strip_section_title))
            if (config.visibleMacros.isEmpty()) {
                Preference(
                    name = stringResource(R.string.kamelot_macro_strip_empty_title),
                    description = stringResource(R.string.kamelot_macro_strip_empty_summary),
                    onClick = {},
                )
            } else {
                config.visibleMacros.sortedBy { it.order }.forEach { item ->
                    val macro = activeProfile.macros.firstOrNull { it.id == item.macroId }
                    val toggle: (Boolean) -> Unit = { enabled ->
                        manager.updateActiveQuickActionsConfig { current ->
                            current.copy(
                                visibleMacros = current.visibleMacros.map { existing ->
                                    if (existing.macroId == item.macroId) existing.copy(enabled = enabled) else existing
                                }
                            )
                        }
                        KeyboardSwitcher.getInstance().setThemeNeedsReload()
                    }
                    Preference(
                        name = item.labelOverride ?: macro?.label ?: item.macroId,
                        description = macro?.textPayload ?: stringResource(R.string.kamelot_macro_missing_summary),
                        onClick = { toggle(!item.enabled) },
                    ) {
                        TextButton(onClick = {
                            manager.moveMacroStripItem(item.macroId, -1)
                            KeyboardSwitcher.getInstance().setThemeNeedsReload()
                        }) { Text(stringResource(R.string.kamelot_move_up_short)) }
                        TextButton(onClick = {
                            manager.moveMacroStripItem(item.macroId, 1)
                            KeyboardSwitcher.getInstance().setThemeNeedsReload()
                        }) { Text(stringResource(R.string.kamelot_move_down_short)) }
                        Switch(checked = item.enabled, onCheckedChange = toggle)
                    }
                }
            }

            PreferenceCategory(stringResource(R.string.kamelot_category_foundation))
            Preference(
                name = stringResource(R.string.kamelot_strip_preview_title),
                description = resolvedItems.joinToString(" | ") { it.label }.ifBlank {
                    stringResource(R.string.kamelot_strip_preview_empty_summary)
                },
                onClick = {},
            )
            Preference(
                name = stringResource(R.string.kamelot_keyboard_builder_title),
                description = stringResource(R.string.kamelot_keyboard_builder_summary),
                onClick = {},
            )
        }
    )

    if (pickMode) {
        ListPickerDialog(
            onDismissRequest = { pickMode = false },
            items = KamelotDefaults.stripModes.toList(),
            selectedItem = config.stripMode,
            title = { androidx.compose.material3.Text(stringResource(R.string.kamelot_strip_mode_title)) },
            onItemSelected = { mode ->
                manager.updateActiveQuickActionsConfig { it.copy(stripMode = mode) }
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
            },
            getItemName = { it.displayName },
        )
    }

    if (pickPreset) {
        ListPickerDialog(
            onDismissRequest = { pickPreset = false },
            items = KamelotDefaults.stripPresets.toList(),
            selectedItem = config.stripPreset,
            title = { androidx.compose.material3.Text(stringResource(R.string.kamelot_strip_preset_title)) },
            onItemSelected = { preset ->
                manager.updateActiveQuickActionsConfig { it.copy(stripPreset = preset) }
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
            },
            getItemName = { it.displayName },
        )
    }

    if (pickPanelPreset) {
        ListPickerDialog(
            onDismissRequest = { pickPanelPreset = false },
            items = KamelotDefaults.quickPanelPresets.toList(),
            selectedItem = config.quickPanelPreset,
            title = { Text(stringResource(R.string.kamelot_quick_panel_preset_title)) },
            onItemSelected = { preset ->
                manager.updateActiveQuickActionsConfig { it.copy(quickPanelPreset = preset) }
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
            },
            getItemName = { it.displayName },
        )
    }

    if (pickBehavior) {
        ListPickerDialog(
            onDismissRequest = { pickBehavior = false },
            items = KamelotDefaults.stripBehaviors.toList(),
            selectedItem = config.stripBehavior,
            title = { Text(stringResource(R.string.kamelot_strip_behavior_title)) },
            onItemSelected = { behavior ->
                manager.updateActiveQuickActionsConfig { it.copy(stripBehavior = behavior) }
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
            },
            getItemName = { it.displayName },
        )
    }

    if (pickRoutingIntent) {
        ListPickerDialog(
            onDismissRequest = { pickRoutingIntent = false },
            items = listOf("none") + KamelotAppCategoryIntent.entries.map { it.name },
            selectedItem = activeProfile.routingIntent.appCategoryIntent?.name ?: "none",
            title = { Text(stringResource(R.string.kamelot_context_profiles_title)) },
            onItemSelected = { intentName ->
                manager.updateActiveProfile {
                    it.copy(
                        routingIntent = it.routingIntent.copy(
                            appCategoryIntent = intentName.takeUnless { key -> key == "none" }?.let(KamelotAppCategoryIntent::valueOf),
                            manualOnly = true,
                        )
                    )
                }
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
            },
            getItemName = {
                if (it == "none") context.getString(R.string.action_none)
                else it.lowercase().replace('_', ' ')
            },
        )
    }

    if (pickMaxVisible) {
        ListPickerDialog(
            onDismissRequest = { pickMaxVisible = false },
            items = (2..8).toList(),
            selectedItem = config.maxVisibleItems,
            title = { Text(stringResource(R.string.kamelot_quick_panel_item_limit_title)) },
            onItemSelected = { limit ->
                manager.updateActiveQuickActionsConfig { it.copy(maxVisibleItems = limit) }
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
            },
            getItemName = { it.toString() },
        )
    }
}

@Preview
@Composable
private fun Preview() {
    initPreview(LocalContext.current)
    Theme(previewDark) {
        Surface {
            KamelotQuickActionsScreen { }
        }
    }
}
