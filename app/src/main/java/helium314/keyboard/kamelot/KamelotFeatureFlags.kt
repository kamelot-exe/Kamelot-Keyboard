// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import android.content.SharedPreferences

object KamelotFeatureFlags {
    const val PREF_ENABLE_EXPERIMENTAL_FEATURES = "kamelot_enable_experimental_features"
    const val PREF_MODULE_PROFILES = "kamelot_module_profiles"
    const val PREF_MODULE_THEMES = "kamelot_module_themes"
    const val PREF_MODULE_QUICK_PANEL = "kamelot_module_quick_panel"
    const val PREF_MODULE_MACROS = "kamelot_module_macros"
    const val PREF_MODULE_GESTURE_OS = "kamelot_module_gesture_os"
    const val PREF_MODULE_HEX_LAB = "kamelot_module_hex_lab"
    const val PREF_SHOW_FUTURE_PROFILES_SECTION = "kamelot_show_future_profiles_section"
    const val PREF_ENABLE_ADVANCED_GESTURE_EXPERIMENTS = "kamelot_enable_advanced_gesture_experiments"
    const val PREF_ENABLE_CUSTOM_PROFILES = "kamelot_enable_custom_profiles"
    const val PREF_ENABLE_CUSTOM_ACTIONS = "kamelot_enable_custom_actions"
    const val PREF_ENABLE_MACRO_STRIP = "kamelot_enable_macro_strip"
    const val PREF_ENABLE_CLIPBOARD_DOCK_ENHANCEMENTS = "kamelot_enable_clipboard_dock_enhancements"
    const val PREF_ENABLE_HEX_LAYOUT = "kamelot_enable_hex_layout"
    const val PREF_ENABLE_ADAPTIVE_HEX_HIT_ZONES = "kamelot_enable_adaptive_hex_hit_zones"
    const val PREF_ENABLE_PREDICTIVE_HEX_SWIPE_PATH = "kamelot_enable_predictive_hex_swipe_path"
    const val PREF_ENABLE_ADVANCED_GESTURES = "kamelot_enable_advanced_gestures"
    const val PREF_ENABLE_LAUNCHER_ACTIONS = "kamelot_enable_launcher_actions"
    const val PREF_ENABLE_THEME_PRESETS_V2 = "kamelot_enable_theme_presets_v2"

    val futureCapabilityKeys = listOf(
        PREF_ENABLE_CUSTOM_PROFILES,
        PREF_ENABLE_CUSTOM_ACTIONS,
        PREF_ENABLE_MACRO_STRIP,
        PREF_ENABLE_CLIPBOARD_DOCK_ENHANCEMENTS,
        PREF_ENABLE_HEX_LAYOUT,
        PREF_ENABLE_ADAPTIVE_HEX_HIT_ZONES,
        PREF_ENABLE_PREDICTIVE_HEX_SWIPE_PATH,
        PREF_ENABLE_ADVANCED_GESTURES,
        PREF_ENABLE_LAUNCHER_ACTIONS,
        PREF_ENABLE_THEME_PRESETS_V2,
    )

    val moduleKeys = listOf(
        PREF_MODULE_PROFILES,
        PREF_MODULE_THEMES,
        PREF_MODULE_QUICK_PANEL,
        PREF_MODULE_MACROS,
        PREF_MODULE_GESTURE_OS,
        PREF_MODULE_HEX_LAB,
    )

    @JvmStatic
    fun isExperimentalFeaturesEnabled(prefs: SharedPreferences): Boolean =
        prefs.getBoolean(PREF_ENABLE_EXPERIMENTAL_FEATURES, KamelotDefaults.ENABLE_EXPERIMENTAL_FEATURES)

    @JvmStatic
    fun isFutureCapabilityEnabled(prefs: SharedPreferences, key: String): Boolean =
        isExperimentalFeaturesEnabled(prefs) && prefs.getBoolean(key, false)

    @JvmStatic
    fun isModuleEnabled(prefs: SharedPreferences, key: String): Boolean =
        prefs.getBoolean(key, true)
}
