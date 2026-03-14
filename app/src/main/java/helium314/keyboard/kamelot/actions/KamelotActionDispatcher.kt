// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.actions

import helium314.keyboard.kamelot.KamelotMacro
import helium314.keyboard.kamelot.KamelotProfile
import helium314.keyboard.kamelot.KamelotQuickActionsResolver
import helium314.keyboard.kamelot.KeyboardAction
import helium314.keyboard.kamelot.KeyboardActionType
import helium314.keyboard.latin.utils.prefs

class KamelotActionDispatcher(
    private val executor: KamelotActionExecutor = KamelotActionExecutor(),
) {
    fun dispatch(action: KeyboardAction, context: KamelotActionContext): KamelotActionResult =
        executor.execute(resolve(action, context.macros), context.copy(macros = context.macros))

    fun resolve(action: KeyboardAction, macros: Map<String, KamelotMacro>): KeyboardAction = when (action.type) {
        KeyboardActionType.RUN_MACRO -> {
            val macro = action.value?.let { macros[it] }
            if (macro != null) KeyboardAction(KeyboardActionType.INSERT_TEXT, macro.textPayload)
            else action
        }
        else -> action
    }

    fun profileActions(profile: KamelotProfile, context: KamelotActionContext? = null): List<KeyboardAction> {
        val prefs = context?.context?.prefs()
        val profiles = context?.profileManager?.loadProfiles().orEmpty().ifEmpty { listOf(profile) }
        val stripActions = prefs?.let { sharedPrefs ->
            KamelotQuickActionsResolver.resolve(profile, sharedPrefs, profiles).map { it.action }
        }.orEmpty()
        return if (stripActions.isNotEmpty()) stripActions else profile.toolbarActions
    }

    fun diagnostics(profile: KamelotProfile, macros: Map<String, KamelotMacro> = emptyMap()): String =
        profileActions(profile).joinToString(", ") { action ->
            val resolved = resolve(action, macros)
            if (resolved == action) action.type.name else "${action.type.name}->${resolved.type.name}"
        }.ifEmpty { "No actions" }
}
