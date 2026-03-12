// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

object KamelotDefaults {
    const val ENABLE_EXPERIMENTAL_FEATURES = false
    const val SHOW_FUTURE_PROFILES_SECTION = false
    const val ENABLE_ADVANCED_GESTURE_EXPERIMENTS = false

    const val DEFAULT_THEME_PRESET_ID = "classic"
    const val DEFAULT_PROFILE_ID = "work"
    const val PROFILES_JSON_DEFAULT = "[]"

    val themePresets: List<ThemePreset>
        get() = ThemePresetMapper.presets

    val profilePresets = listOf(
        KamelotProfile(
            id = "chat",
            name = "Chat",
            themePreset = "glass_neon",
            layoutMode = FutureLayoutMode.STANDARD,
            gestureConfig = KamelotGestureConfig(gestureTypingEnabled = true, advancedGesturesEnabled = false),
            toolbarActions = listOf(
                KeyboardAction(KeyboardActionType.OPEN_CLIPBOARD),
                KeyboardAction(KeyboardActionType.RUN_MACRO, "chat_smile"),
            ),
            macros = listOf(
                KamelotMacro(id = "chat_smile", label = "Smile", textPayload = ":)")
            )
        ),
        KamelotProfile(
            id = DEFAULT_PROFILE_ID,
            name = "Work",
            themePreset = DEFAULT_THEME_PRESET_ID,
            layoutMode = FutureLayoutMode.STANDARD,
            gestureConfig = KamelotGestureConfig(gestureTypingEnabled = true, advancedGesturesEnabled = false),
            toolbarActions = listOf(
                KeyboardAction(KeyboardActionType.MOVE_CURSOR_LEFT),
                KeyboardAction(KeyboardActionType.MOVE_CURSOR_RIGHT),
            )
        ),
        KamelotProfile(
            id = "minimal",
            name = "Minimal",
            themePreset = "minimal",
            layoutMode = FutureLayoutMode.STANDARD,
            gestureConfig = KamelotGestureConfig(gestureTypingEnabled = true, advancedGesturesEnabled = false),
            toolbarActions = emptyList(),
            macros = listOf(
                KamelotMacro(id = "minimal_signature", label = "Signature", textPayload = "Sent from Kamelot Keyboard")
            )
        ),
        KamelotProfile(
            id = "one_hand",
            name = "OneHand",
            themePreset = "brutal_black",
            layoutMode = FutureLayoutMode.STANDARD,
            gestureConfig = KamelotGestureConfig(gestureTypingEnabled = true, advancedGesturesEnabled = false),
            toolbarActions = listOf(
                KeyboardAction(KeyboardActionType.SWITCH_PROFILE, DEFAULT_PROFILE_ID)
            )
        ),
    )

    val defaultLayoutMode = FutureLayoutMode.STANDARD
}
