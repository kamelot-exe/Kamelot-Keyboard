// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import helium314.keyboard.latin.R

@Composable
fun KamelotProfileEditorDialog(
    initialName: String,
    titleRes: Int,
    summaryRes: Int,
    confirmRes: Int,
    onDismissRequest: () -> Unit,
    onSave: (String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    ThreeButtonAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirmed = { onSave(name.trim()) },
        title = { Text(stringResource(titleRes)) },
        confirmButtonText = stringResource(confirmRes),
        checkOk = { name.isNotBlank() },
        content = {
            Column {
                Text(stringResource(summaryRes))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.kamelot_profile_name_title)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                )
            }
        },
    )
}
