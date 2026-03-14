// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import helium314.keyboard.kamelot.KamelotMacro
import helium314.keyboard.latin.R

@Composable
fun KamelotMacroEditorDialog(
    initialMacro: KamelotMacro?,
    onDismissRequest: () -> Unit,
    onSave: (label: String, textPayload: String, existingId: String?) -> Unit,
    onDelete: ((KamelotMacro) -> Unit)? = null,
) {
    var label by rememberSaveable { mutableStateOf(initialMacro?.label.orEmpty()) }
    var textPayload by rememberSaveable { mutableStateOf(initialMacro?.textPayload.orEmpty()) }
    val isEditing = initialMacro != null
    ThreeButtonAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirmed = { onSave(label.trim(), textPayload, initialMacro?.id) },
        title = {
            Text(
                stringResource(
                    if (isEditing) R.string.kamelot_macro_editor_title_edit
                    else R.string.kamelot_macro_editor_title_add
                )
            )
        },
        neutralButtonText = if (isEditing && onDelete != null) stringResource(R.string.button_delete) else null,
        onNeutral = { initialMacro?.let { onDelete?.invoke(it) } },
        checkOk = { label.isNotBlank() && textPayload.isNotBlank() },
        content = {
            Column {
                Text(stringResource(R.string.kamelot_macro_editor_summary))
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.kamelot_macro_label_title)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = textPayload,
                    onValueChange = { textPayload = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.kamelot_macro_payload_title)) },
                    singleLine = false,
                    minLines = 3,
                )
            }
        },
    )
}
