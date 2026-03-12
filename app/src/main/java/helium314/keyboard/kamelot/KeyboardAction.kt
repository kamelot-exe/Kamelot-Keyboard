// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import kotlinx.serialization.Serializable

@Serializable
data class KeyboardAction(
    val type: KeyboardActionType,
    val value: String? = null,
)

@Serializable
enum class KeyboardActionType {
    INSERT_TEXT,
    DELETE_WORD,
    MOVE_CURSOR_LEFT,
    MOVE_CURSOR_RIGHT,
    OPEN_CLIPBOARD,
    RUN_MACRO,
    SWITCH_PROFILE,
}
