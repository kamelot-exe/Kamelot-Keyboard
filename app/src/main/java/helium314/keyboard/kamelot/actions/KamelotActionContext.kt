// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.actions

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import helium314.keyboard.kamelot.KamelotMacro
import helium314.keyboard.kamelot.KamelotProfileManager

data class KamelotActionContext(
    val context: Context,
    val inputConnection: InputConnection? = null,
    val editorInfo: EditorInfo? = null,
    val selectedTextProvider: (() -> CharSequence?)? = null,
    val profileManager: KamelotProfileManager? = null,
    val macros: Map<String, KamelotMacro> = emptyMap(),
    val openClipboard: (() -> Boolean)? = null,
    val onProfileSwitched: ((String) -> Unit)? = null,
    val dryRun: Boolean = false,
) {
    fun selectedText(): CharSequence? = selectedTextProvider?.invoke()
}
