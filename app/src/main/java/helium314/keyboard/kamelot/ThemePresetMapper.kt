// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import helium314.keyboard.keyboard.KeyboardTheme
import helium314.keyboard.latin.settings.Settings
import helium314.keyboard.latin.utils.prefs

object ThemePresetMapper {
    const val PREF_ACTIVE_THEME_PRESET_ID = "kamelot_active_theme_preset_id"

    val presets = listOf(
        ThemePreset(
            id = "classic",
            displayName = "Classic",
            baseThemeId = KeyboardTheme.STYLE_MATERIAL,
            accentColor = null,
            backgroundStyle = ThemeBackgroundStyle.SOLID,
            keyShape = ThemeKeyShape.SYSTEM,
            dayThemeName = KeyboardTheme.THEME_LIGHT,
            nightThemeName = KeyboardTheme.THEME_DARK,
            borderStyle = ThemeBorderStyle.SOFT,
            glowIntensity = ThemeGlowIntensity.OFF,
            description = "Balanced default with clean contrast and familiar proportions.",
        ),
        ThemePreset(
            id = "brutal_black",
            displayName = "Brutal Black",
            baseThemeId = KeyboardTheme.STYLE_MATERIAL,
            accentColor = "#000000",
            backgroundStyle = ThemeBackgroundStyle.CONTRAST,
            keyShape = ThemeKeyShape.SHARP,
            dayThemeName = KeyboardTheme.THEME_BLACK,
            nightThemeName = KeyboardTheme.THEME_BLACK,
            borderStyle = ThemeBorderStyle.SHARP,
            glowIntensity = ThemeGlowIntensity.OFF,
            description = "Hard-edged, high-density dark theme with deep blacks and minimal distraction.",
        ),
        ThemePreset(
            id = "glass_neon",
            displayName = "Glass Neon",
            baseThemeId = KeyboardTheme.STYLE_ROUNDED,
            accentColor = "#ff60ff",
            backgroundStyle = ThemeBackgroundStyle.GLASS,
            keyShape = ThemeKeyShape.ROUNDED,
            dayThemeName = KeyboardTheme.THEME_OCEAN,
            nightThemeName = KeyboardTheme.THEME_VIOLETTE,
            useKeyBorders = true,
            borderStyle = ThemeBorderStyle.SOFT,
            glowIntensity = ThemeGlowIntensity.VIVID,
            description = "Rounded glassy surface with brighter accents and a richer night presence.",
        ),
        ThemePreset(
            id = "minimal_dark",
            displayName = "Minimal Dark",
            baseThemeId = KeyboardTheme.STYLE_MATERIAL,
            accentColor = "#263238",
            backgroundStyle = ThemeBackgroundStyle.SOLID,
            keyShape = ThemeKeyShape.SYSTEM,
            dayThemeName = KeyboardTheme.THEME_CLOUDY,
            nightThemeName = KeyboardTheme.THEME_DARKER,
            borderStyle = ThemeBorderStyle.SOFT,
            glowIntensity = ThemeGlowIntensity.OFF,
            description = "Quiet low-glare preset tuned for focused long-form typing.",
        ),
        ThemePreset(
            id = "high_contrast",
            displayName = "High Contrast",
            baseThemeId = KeyboardTheme.STYLE_MATERIAL,
            accentColor = "#ffffff",
            backgroundStyle = ThemeBackgroundStyle.CONTRAST,
            keyShape = ThemeKeyShape.SHARP,
            dayThemeName = KeyboardTheme.THEME_BLACK,
            nightThemeName = KeyboardTheme.THEME_BLACK,
            useKeyBorders = true,
            borderStyle = ThemeBorderStyle.HIGH_CONTRAST,
            glowIntensity = ThemeGlowIntensity.SUBTLE,
            description = "Sharper separation between rows and keys for readability and accessibility.",
        ),
    )

    private val presetMap = presets.associateBy { it.id }

    fun getPreset(id: String?): ThemePreset =
        presetMap[if (id == "minimal") "minimal_dark" else id]
            ?: presetMap.getValue(KamelotDefaults.DEFAULT_THEME_PRESET_ID)

    fun getActivePreset(prefs: SharedPreferences): ThemePreset =
        getPreset(prefs.getString(PREF_ACTIVE_THEME_PRESET_ID, KamelotDefaults.DEFAULT_THEME_PRESET_ID))

    fun applyPreset(context: Context, presetId: String, prefs: SharedPreferences = context.prefs()): ThemePreset {
        val preset = getPreset(presetId)
        prefs.edit {
            putString(PREF_ACTIVE_THEME_PRESET_ID, preset.id)
            putString(Settings.PREF_THEME_STYLE, preset.baseThemeId)
            putString(Settings.PREF_THEME_COLORS, preset.dayThemeName)
            putString(Settings.PREF_THEME_COLORS_NIGHT, preset.nightThemeName)
            putBoolean(Settings.PREF_THEME_KEY_BORDERS, preset.useKeyBorders)
        }
        return preset
    }
}
