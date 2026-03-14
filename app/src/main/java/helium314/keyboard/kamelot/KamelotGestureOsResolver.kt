// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import android.content.SharedPreferences

object KamelotGestureOsResolver {
    fun resolve(profile: KamelotProfile, prefs: SharedPreferences): List<GestureBinding> {
        if (!KamelotFeatureFlags.isModuleEnabled(prefs, KamelotFeatureFlags.PREF_MODULE_GESTURE_OS)) {
            return emptyList()
        }
        if (!KamelotFeatureFlags.isFutureCapabilityEnabled(prefs, KamelotFeatureFlags.PREF_ENABLE_ADVANCED_GESTURES)) {
            return emptyList()
        }
        return profile.gestureActionConfig.bindings.filter { it.enabled }
    }

    fun findBinding(
        profile: KamelotProfile,
        prefs: SharedPreferences,
        triggerType: GestureTriggerType,
    ): GestureBinding? = resolve(profile, prefs).firstOrNull { it.triggerType == triggerType }
}
