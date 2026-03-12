// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.actions

import helium314.keyboard.kamelot.KeyboardAction
import helium314.keyboard.kamelot.KeyboardActionType

class KamelotActionExecutor {
    fun execute(action: KeyboardAction, context: KamelotActionContext): KamelotActionResult = when (action.type) {
        KeyboardActionType.INSERT_TEXT -> insertText(action.value, context)
        KeyboardActionType.MOVE_CURSOR_LEFT -> moveCursor(-1, context)
        KeyboardActionType.MOVE_CURSOR_RIGHT -> moveCursor(1, context)
        KeyboardActionType.SWITCH_PROFILE -> switchProfile(action.value, context)
        KeyboardActionType.OPEN_CLIPBOARD -> openClipboard(context)
        KeyboardActionType.RUN_MACRO -> runMacro(action.value, context)
        KeyboardActionType.DELETE_WORD -> KamelotActionResult.Unsupported("DELETE_WORD is deferred for a later phase.")
    }

    private fun insertText(value: String?, context: KamelotActionContext): KamelotActionResult {
        val text = value?.takeIf { it.isNotEmpty() }
            ?: return KamelotActionResult.Unsupported("INSERT_TEXT requires a non-empty payload.")
        if (context.dryRun) return KamelotActionResult.Success("Dry-run insert text.")
        val inputConnection = context.inputConnection
            ?: return KamelotActionResult.Ignored("No InputConnection available for text insertion.")
        return runCatching {
            inputConnection.commitText(text, 1)
            KamelotActionResult.Success("Inserted text.")
        }.getOrElse {
            KamelotActionResult.FailedSafely("Failed to insert text safely.")
        }
    }

    private fun moveCursor(direction: Int, context: KamelotActionContext): KamelotActionResult {
        if (context.dryRun) return KamelotActionResult.Success("Dry-run move cursor ${if (direction < 0) "left" else "right"}.")
        val inputConnection = context.inputConnection
            ?: return KamelotActionResult.Ignored("No InputConnection available for cursor movement.")
        val selectedTextLength = context.selectedText()?.length ?: 0
        val delta = if (selectedTextLength > 0) {
            if (direction < 0) -selectedTextLength else selectedTextLength
        } else direction
        val extracted = runCatching { inputConnection.getExtractedText(null, 0) }.getOrNull()
            ?: return KamelotActionResult.Ignored("Cursor position is unavailable.")
        val selectionEnd = extracted.selectionEnd.takeIf { it >= 0 }
            ?: return KamelotActionResult.Ignored("Selection end is unavailable.")
        val newPosition = (selectionEnd + delta).coerceAtLeast(0)
        return runCatching {
            if (!inputConnection.setSelection(newPosition, newPosition)) {
                KamelotActionResult.Ignored("InputConnection rejected cursor movement.")
            } else {
                KamelotActionResult.Success("Moved cursor.")
            }
        }.getOrElse {
            KamelotActionResult.FailedSafely("Failed to move cursor safely.")
        }
    }

    private fun switchProfile(profileId: String?, context: KamelotActionContext): KamelotActionResult {
        val id = profileId?.takeIf { it.isNotBlank() }
            ?: return KamelotActionResult.Unsupported("SWITCH_PROFILE requires a target profile id.")
        if (context.dryRun) return KamelotActionResult.Success("Dry-run switch profile to $id.")
        val manager = context.profileManager
            ?: return KamelotActionResult.Ignored("No profile manager available.")
        return runCatching {
            val profile = manager.switchProfile(id)
            KamelotActionResult.Success("Switched profile to ${profile.name}.")
        }.getOrElse {
            KamelotActionResult.FailedSafely("Failed to switch profile safely.")
        }
    }

    private fun openClipboard(context: KamelotActionContext): KamelotActionResult {
        if (context.dryRun) return KamelotActionResult.Success("Dry-run open clipboard.")
        val hook = context.openClipboard
            ?: return KamelotActionResult.Unsupported("OPEN_CLIPBOARD has no runtime hook yet.")
        return runCatching {
            if (hook()) KamelotActionResult.Success("Opened clipboard.")
            else KamelotActionResult.Ignored("Clipboard hook declined to open.")
        }.getOrElse {
            KamelotActionResult.FailedSafely("Failed to open clipboard safely.")
        }
    }

    private fun runMacro(value: String?, context: KamelotActionContext): KamelotActionResult {
        val payload = context.macros[value]?.textPayload ?: value
        return insertText(payload, context).let {
            when (it) {
                is KamelotActionResult.Success -> KamelotActionResult.Success("Executed macro text payload.")
                is KamelotActionResult.Ignored -> it
                is KamelotActionResult.FailedSafely -> it
                is KamelotActionResult.Unsupported -> KamelotActionResult.Unsupported("RUN_MACRO requires a macro id or text payload.")
            }
        }
    }
}
