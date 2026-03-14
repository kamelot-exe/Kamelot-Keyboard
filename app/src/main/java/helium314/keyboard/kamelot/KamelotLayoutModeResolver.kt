// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import android.content.Context
import android.content.SharedPreferences
import helium314.keyboard.latin.utils.prefs

object KamelotLayoutModeResolver {
    @JvmStatic
    @JvmOverloads
    fun resolveActiveLayoutMode(
        context: Context,
        prefs: SharedPreferences = context.prefs(),
    ): FutureLayoutMode {
        val requestedMode = KamelotProfileManager(context, prefs).getActiveProfile().layoutMode
        return when {
            requestedMode != FutureLayoutMode.HEX_EXPERIMENTAL -> requestedMode
            !KamelotFeatureFlags.isModuleEnabled(prefs, KamelotFeatureFlags.PREF_MODULE_HEX_LAB) -> FutureLayoutMode.STANDARD
            KamelotFeatureFlags.isFutureCapabilityEnabled(prefs, KamelotFeatureFlags.PREF_ENABLE_HEX_LAYOUT) -> requestedMode
            else -> FutureLayoutMode.STANDARD
        }
    }

    @JvmStatic
    @JvmOverloads
    fun isHexLayoutEnabled(
        context: Context,
        prefs: SharedPreferences = context.prefs(),
    ): Boolean = resolveActiveLayoutMode(context, prefs) == FutureLayoutMode.HEX_EXPERIMENTAL
}
