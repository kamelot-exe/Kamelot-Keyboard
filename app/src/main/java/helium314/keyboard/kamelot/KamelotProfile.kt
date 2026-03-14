// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import kotlinx.serialization.Serializable

@Serializable
data class KamelotProfile(
    val id: String,
    val name: String,
    val themePreset: String,
    val layoutMode: FutureLayoutMode,
    val gestureConfig: KamelotGestureConfig,
    val toolbarActions: List<KeyboardAction>,
    val macros: List<KamelotMacro> = emptyList(),
    val quickActionsConfig: QuickActionsConfig = QuickActionsConfig(),
    val contextHints: List<KamelotContextCategory> = listOf(KamelotContextCategory.GENERAL),
    val routingIntent: KamelotProfileRoutingIntent = KamelotProfileRoutingIntent(),
    val gestureActionConfig: GestureActionConfig = GestureActionConfig(),
)

@Serializable
data class KamelotGestureConfig(
    val gestureTypingEnabled: Boolean = true,
    val advancedGesturesEnabled: Boolean = false,
)
