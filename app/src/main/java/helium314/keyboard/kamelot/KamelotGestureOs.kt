// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import kotlinx.serialization.Serializable

@Serializable
enum class GestureTriggerType {
    SPACE_SWIPE_LEFT,
    SPACE_SWIPE_RIGHT,
    SPACE_SWIPE_UP,
}

@Serializable
enum class GestureScope {
    SPACEBAR,
    PROFILE,
}

@Serializable
enum class GestureOsPreset {
    OFF,
    CURSOR,
    CHAT,
    ONE_HAND,
}

@Serializable
data class GestureBinding(
    val triggerType: GestureTriggerType,
    val action: KeyboardAction,
    val scope: GestureScope = GestureScope.SPACEBAR,
    val enabled: Boolean = true,
)

@Serializable
data class GestureActionConfig(
    val preset: GestureOsPreset = GestureOsPreset.OFF,
    val bindings: List<GestureBinding> = emptyList(),
)

val GestureTriggerType.displayName: String
    get() = when (this) {
        GestureTriggerType.SPACE_SWIPE_LEFT -> "Space swipe left"
        GestureTriggerType.SPACE_SWIPE_RIGHT -> "Space swipe right"
        GestureTriggerType.SPACE_SWIPE_UP -> "Space swipe up"
    }

val GestureOsPreset.displayName: String
    get() = when (this) {
        GestureOsPreset.OFF -> "Off"
        GestureOsPreset.CURSOR -> "Cursor"
        GestureOsPreset.CHAT -> "Chat"
        GestureOsPreset.ONE_HAND -> "One-hand"
    }
