// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit
import helium314.keyboard.kamelot.KamelotDefaults
import helium314.keyboard.kamelot.KamelotFeatureFlags
import helium314.keyboard.kamelot.KamelotGestureOsResolver
import helium314.keyboard.kamelot.KamelotProfileManager
import helium314.keyboard.kamelot.GestureOsPreset
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
fun KamelotGestureOsScreen(onClickBack: () -> Unit) {
    val context = LocalContext.current
    val manager = remember(context) { KamelotProfileManager(context) }
    val prefChanges = (context.getActivity() as? SettingsActivity)?.prefChanged?.collectAsState()
    if ((prefChanges?.value ?: 0) < 0) Log.v("kamelot", "pref recompose")
    val prefs = context.prefs()
    val activeProfile = manager.getActiveProfile()
    val bindings = KamelotGestureOsResolver.resolve(activeProfile, prefs)
    var pickPreset by remember { mutableStateOf(false) }

    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.kamelot_gesture_os_title),
        settings = emptyList(),
        content = {
            PreferenceCategory(stringResource(R.string.kamelot_category_configuration))
            Preference(
                name = stringResource(R.string.kamelot_active_profile_title),
                description = activeProfile.name,
                onClick = {},
            )
            Preference(
                name = stringResource(R.string.kamelot_gesture_os_enable_title),
                description = stringResource(R.string.kamelot_gesture_os_enable_summary),
                onClick = {},
            ) {
                Switch(
                    checked = KamelotFeatureFlags.isFutureCapabilityEnabled(prefs, KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURES),
                    onCheckedChange = {
                        prefs.edit { putBoolean(KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURES, it) }
                        KeyboardSwitcher.getInstance().setThemeNeedsReload()
                    }
                )
            }
            Preference(
                name = stringResource(R.string.kamelot_gesture_os_preset_title),
                description = activeProfile.gestureActionConfig.preset.displayName,
                onClick = { pickPreset = true },
            )
            Preference(
                name = stringResource(R.string.kamelot_gesture_os_scope_title),
                description = stringResource(R.string.kamelot_gesture_os_scope_summary),
                onClick = {},
            )

            PreferenceCategory(stringResource(R.string.kamelot_gesture_os_bindings_title))
            if (bindings.isEmpty()) {
                Preference(
                    name = stringResource(R.string.kamelot_gesture_os_bindings_empty_title),
                    description = stringResource(R.string.kamelot_gesture_os_bindings_empty_summary),
                    onClick = {},
                )
            } else {
                bindings.forEach { binding ->
                    Preference(
                        name = binding.triggerType.displayName,
                        description = binding.action.type.name.replace('_', ' ').lowercase(),
                        onClick = {},
                    )
                }
            }
        }
    )

    if (pickPreset) {
        ListPickerDialog(
            onDismissRequest = { pickPreset = false },
            items = KamelotDefaults.gestureOsPresets.toList(),
            selectedItem = activeProfile.gestureActionConfig.preset,
            title = { Text(stringResource(R.string.kamelot_gesture_os_preset_title)) },
            onItemSelected = { preset ->
                val seeded = KamelotDefaults.profilePresets.firstOrNull { it.id == activeProfile.id }?.gestureActionConfig
                manager.updateActiveGestureActionConfig {
                    when (preset) {
                        GestureOsPreset.OFF -> it.copy(preset = preset, bindings = emptyList())
                        else -> (seeded ?: it).copy(preset = preset)
                    }
                }
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
            },
            getItemName = { it.displayName },
        )
    }
}

@Preview
@Composable
private fun Preview() {
    initPreview(LocalContext.current)
    Theme(previewDark) {
        Surface {
            KamelotGestureOsScreen { }
        }
    }
}
