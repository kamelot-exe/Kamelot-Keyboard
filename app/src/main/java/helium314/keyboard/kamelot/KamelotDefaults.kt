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
                KamelotMacro(id = "chat_smile", label = "Smile", textPayload = ":)"),
                KamelotMacro(id = "chat_omw", label = "On my way", textPayload = "On my way."),
            ),
            quickActionsConfig = QuickActionsConfig(
                stripMode = KamelotStripMode.PROFILE_DRIVEN,
                stripPreset = KamelotStripPreset.COMMUNICATION,
                quickPanelPreset = KamelotQuickPanelPreset.BALANCED,
                stripBehavior = KamelotStripBehavior.ALONGSIDE_SUGGESTIONS,
                macroStripEnabled = true,
                showProfileSwitcher = true,
                showProfileBadge = true,
                maxVisibleItems = 6,
                visibleQuickActions = listOf(
                    QuickActionItem(
                        id = "chat_clipboard",
                        label = "Clipboard",
                        action = KeyboardAction(KeyboardActionType.OPEN_CLIPBOARD),
                        order = 0,
                    ),
                    QuickActionItem(
                        id = "chat_work",
                        label = "Work",
                        action = KeyboardAction(KeyboardActionType.SWITCH_PROFILE, DEFAULT_PROFILE_ID),
                        order = 1,
                    ),
                ),
                visibleMacros = listOf(
                    MacroStripItem(macroId = "chat_smile", order = 2),
                    MacroStripItem(macroId = "chat_omw", order = 3),
                ),
            ),
            contextHints = listOf(KamelotContextCategory.CHAT, KamelotContextCategory.GENERAL),
            routingIntent = KamelotProfileRoutingIntent(
                appCategoryIntent = KamelotAppCategoryIntent.CHAT,
                manualOnly = true,
            ),
            gestureActionConfig = GestureActionConfig(
                preset = GestureOsPreset.CHAT,
                bindings = listOf(
                    GestureBinding(
                        triggerType = GestureTriggerType.SPACE_SWIPE_LEFT,
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_LEFT),
                    ),
                    GestureBinding(
                        triggerType = GestureTriggerType.SPACE_SWIPE_RIGHT,
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_RIGHT),
                    ),
                    GestureBinding(
                        triggerType = GestureTriggerType.SPACE_SWIPE_UP,
                        action = KeyboardAction(KeyboardActionType.OPEN_CLIPBOARD),
                    ),
                ),
            ),
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
            ,
            macros = listOf(
                KamelotMacro(id = "work_signature", label = "Signature", textPayload = "Best regards,"),
                KamelotMacro(id = "work_email", label = "Email", textPayload = "Thanks, I will follow up by email."),
            ),
            quickActionsConfig = QuickActionsConfig(
                stripMode = KamelotStripMode.PROFILE_DRIVEN,
                stripPreset = KamelotStripPreset.PRODUCTIVITY,
                quickPanelPreset = KamelotQuickPanelPreset.FOCUSED,
                stripBehavior = KamelotStripBehavior.ALONGSIDE_SUGGESTIONS,
                macroStripEnabled = true,
                showProfileSwitcher = true,
                showProfileBadge = true,
                maxVisibleItems = 6,
                visibleQuickActions = listOf(
                    QuickActionItem(
                        id = "work_left",
                        label = "Left",
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_LEFT),
                        order = 0,
                    ),
                    QuickActionItem(
                        id = "work_right",
                        label = "Right",
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_RIGHT),
                        order = 1,
                    ),
                    QuickActionItem(
                        id = "work_clipboard",
                        label = "Clipboard",
                        action = KeyboardAction(KeyboardActionType.OPEN_CLIPBOARD),
                        order = 2,
                    ),
                ),
                visibleMacros = listOf(
                    MacroStripItem(macroId = "work_signature", order = 3),
                    MacroStripItem(macroId = "work_email", order = 4),
                ),
            ),
            contextHints = listOf(KamelotContextCategory.WORK, KamelotContextCategory.CODING, KamelotContextCategory.NOTES),
            routingIntent = KamelotProfileRoutingIntent(
                appCategoryIntent = KamelotAppCategoryIntent.WORK,
                manualOnly = true,
            ),
            gestureActionConfig = GestureActionConfig(
                preset = GestureOsPreset.CURSOR,
                bindings = listOf(
                    GestureBinding(
                        triggerType = GestureTriggerType.SPACE_SWIPE_LEFT,
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_LEFT),
                    ),
                    GestureBinding(
                        triggerType = GestureTriggerType.SPACE_SWIPE_RIGHT,
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_RIGHT),
                    ),
                ),
            ),
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
            ),
            quickActionsConfig = QuickActionsConfig(
                stripMode = KamelotStripMode.MINIMAL,
                stripPreset = KamelotStripPreset.COMPACT,
                quickPanelPreset = KamelotQuickPanelPreset.MINIMAL,
                stripBehavior = KamelotStripBehavior.ONLY_WHEN_IDLE,
                macroStripEnabled = false,
                showProfileSwitcher = true,
                showProfileBadge = true,
                maxVisibleItems = 3,
                visibleQuickActions = emptyList(),
                visibleMacros = listOf(
                    MacroStripItem(macroId = "minimal_signature", enabled = false, order = 0)
                ),
            ),
            contextHints = listOf(KamelotContextCategory.NOTES, KamelotContextCategory.BROWSING),
            routingIntent = KamelotProfileRoutingIntent(
                appCategoryIntent = KamelotAppCategoryIntent.NOTES,
                manualOnly = true,
            ),
            gestureActionConfig = GestureActionConfig(
                preset = GestureOsPreset.OFF,
            ),
        ),
        KamelotProfile(
            id = "one_hand",
            name = "OneHand",
            themePreset = "brutal_black",
            layoutMode = FutureLayoutMode.STANDARD,
            gestureConfig = KamelotGestureConfig(gestureTypingEnabled = true, advancedGesturesEnabled = false),
            toolbarActions = listOf(
                KeyboardAction(KeyboardActionType.SWITCH_PROFILE, DEFAULT_PROFILE_ID)
            ),
            quickActionsConfig = QuickActionsConfig(
                stripMode = KamelotStripMode.QUICK_ACTIONS_ONLY,
                stripPreset = KamelotStripPreset.COMPACT,
                quickPanelPreset = KamelotQuickPanelPreset.SWITCHER,
                stripBehavior = KamelotStripBehavior.ALONGSIDE_SUGGESTIONS,
                macroStripEnabled = false,
                showProfileSwitcher = true,
                showProfileBadge = true,
                maxVisibleItems = 4,
                visibleQuickActions = listOf(
                    QuickActionItem(
                        id = "one_hand_left",
                        label = "Left",
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_LEFT),
                        order = 0,
                    ),
                    QuickActionItem(
                        id = "one_hand_right",
                        label = "Right",
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_RIGHT),
                        order = 1,
                    ),
                    QuickActionItem(
                        id = "one_hand_work",
                        label = "Work",
                        action = KeyboardAction(KeyboardActionType.SWITCH_PROFILE, DEFAULT_PROFILE_ID),
                        order = 2,
                    ),
                ),
            ),
            contextHints = listOf(KamelotContextCategory.GENERAL, KamelotContextCategory.BROWSING),
            routingIntent = KamelotProfileRoutingIntent(
                appCategoryIntent = KamelotAppCategoryIntent.BROWSER,
                manualOnly = true,
            ),
            gestureActionConfig = GestureActionConfig(
                preset = GestureOsPreset.ONE_HAND,
                bindings = listOf(
                    GestureBinding(
                        triggerType = GestureTriggerType.SPACE_SWIPE_LEFT,
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_LEFT),
                    ),
                    GestureBinding(
                        triggerType = GestureTriggerType.SPACE_SWIPE_RIGHT,
                        action = KeyboardAction(KeyboardActionType.MOVE_CURSOR_RIGHT),
                    ),
                ),
            ),
        ),
    )

    val defaultLayoutMode = FutureLayoutMode.STANDARD

    val stripPresets = KamelotStripPreset.entries
    val stripModes = KamelotStripMode.entries
    val quickPanelPresets = KamelotQuickPanelPreset.entries
    val stripBehaviors = KamelotStripBehavior.entries
    val gestureOsPresets = GestureOsPreset.entries
}
