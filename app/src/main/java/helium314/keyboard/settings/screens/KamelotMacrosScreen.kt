// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import helium314.keyboard.kamelot.KamelotMacro
import helium314.keyboard.kamelot.KamelotProfileManager
import helium314.keyboard.keyboard.KeyboardSwitcher
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.Log
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.getActivity
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.settings.SettingsActivity
import helium314.keyboard.settings.dialogs.KamelotMacroEditorDialog
import helium314.keyboard.settings.initPreview
import helium314.keyboard.settings.preferences.Preference
import helium314.keyboard.settings.preferences.PreferenceCategory

@Composable
fun KamelotMacrosScreen(onClickBack: () -> Unit) {
    val context = LocalContext.current
    val manager = remember(context) { KamelotProfileManager(context) }
    val prefChanges = (context.getActivity() as? SettingsActivity)?.prefChanged?.collectAsState()
    if ((prefChanges?.value ?: 0) < 0) Log.v("kamelot", "pref recompose")
    val activeProfile = manager.getActiveProfile()
    var editingMacro by remember { mutableStateOf<KamelotMacro?>(null) }
    var creatingMacro by remember { mutableStateOf(false) }

    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.kamelot_macros_title),
        settings = emptyList(),
        content = {
            PreferenceCategory(stringResource(R.string.kamelot_category_configuration))
            Preference(
                name = stringResource(R.string.kamelot_active_profile_title),
                description = activeProfile.name,
                onClick = {},
            )
            Preference(
                name = stringResource(R.string.kamelot_macro_add_title),
                description = stringResource(R.string.kamelot_macro_add_summary),
                onClick = { creatingMacro = true },
            )
            PreferenceCategory(stringResource(R.string.kamelot_macro_list_title))
            if (activeProfile.macros.isEmpty()) {
                Preference(
                    name = stringResource(R.string.kamelot_macro_empty_title),
                    description = stringResource(R.string.kamelot_macro_empty_summary),
                    onClick = { creatingMacro = true },
                )
            } else {
                activeProfile.macros.sortedBy { it.label.lowercase() }.forEach { macro ->
                    Preference(
                        name = macro.label,
                        description = macro.textPayload,
                        onClick = { editingMacro = macro },
                    )
                }
            }
        }
    )

    if (creatingMacro) {
        KamelotMacroEditorDialog(
            initialMacro = null,
            onDismissRequest = { creatingMacro = false },
            onSave = { label, textPayload, _ ->
                manager.upsertMacro(macro = KamelotMacro(buildMacroId(label, activeProfile.macros), label, textPayload))
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
                creatingMacro = false
            },
        )
    }

    editingMacro?.let { macro ->
        KamelotMacroEditorDialog(
            initialMacro = macro,
            onDismissRequest = { editingMacro = null },
            onSave = { label, textPayload, existingId ->
                manager.upsertMacro(
                    macro = KamelotMacro(existingId ?: buildMacroId(label, activeProfile.macros), label, textPayload)
                )
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
                editingMacro = null
            },
            onDelete = {
                manager.deleteMacro(macroId = it.id)
                KeyboardSwitcher.getInstance().setThemeNeedsReload()
                editingMacro = null
            },
        )
    }
}

private fun buildMacroId(label: String, existingMacros: List<KamelotMacro>): String {
    val base = label.lowercase()
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')
        .ifBlank { "macro" }
    if (existingMacros.none { it.id == base }) return base
    var index = 2
    while (existingMacros.any { it.id == "${base}_$index" }) index++
    return "${base}_$index"
}

@Preview
@Composable
private fun Preview() {
    initPreview(LocalContext.current)
    Theme(previewDark) {
        Surface {
            KamelotMacrosScreen { }
        }
    }
}
