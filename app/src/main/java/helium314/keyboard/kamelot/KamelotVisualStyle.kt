// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import helium314.keyboard.keyboard.KeyboardTheme
import helium314.keyboard.latin.common.ColorType
import helium314.keyboard.latin.utils.isBrightColor
import helium314.keyboard.latin.utils.prefs

data class KamelotVisualStyle(
    val keyCornerRadius: Float,
    val outlineWidth: Float,
    val fillColor: Int,
    val pressedFillColor: Int,
    val strokeColor: Int,
    val pressedStrokeColor: Int,
    val accentColor: Int,
    val glowColor: Int,
    val glowRadius: Float,
)

object KamelotVisualStyleResolver {
    @JvmStatic
    fun resolve(context: Context): KamelotVisualStyle {
        val prefs = context.prefs()
        val preset = ThemePresetMapper.getActivePreset(prefs)
        val profile = runCatching { KamelotProfileManager(context).getActiveProfile() }.getOrNull()
        val appearance = profile?.appearanceConfig ?: appearanceConfigForPreset(preset)
        val density = context.resources.displayMetrics.density
        val colors = KeyboardTheme.getColorsForCurrentTheme(context)
        val keyBackground = colors.get(ColorType.KEY_BACKGROUND)
        val fallbackAccent = colors.get(ColorType.GESTURE_TRAIL)
        val accent = runCatching { preset.accentColor?.toColorInt() ?: fallbackAccent }.getOrDefault(fallbackAccent)
        val isDark = !isBrightColor(keyBackground)

        val resolvedCorner = when (appearance.cornerStyle) {
            KamelotCornerStyle.MATCH_PRESET -> when (preset.keyShape) {
                ThemeKeyShape.SYSTEM -> 18f
                ThemeKeyShape.ROUNDED -> 24f
                ThemeKeyShape.SHARP -> 12f
            }
            KamelotCornerStyle.SYSTEM -> 18f
            KamelotCornerStyle.ROUNDED -> 24f
            KamelotCornerStyle.SHARP -> 10f
        } * density

        val resolvedBorder = when (appearance.borderStyle) {
            KamelotBorderStyle.MATCH_PRESET -> when (preset.borderStyle) {
                ThemeBorderStyle.SOFT -> 0
                ThemeBorderStyle.SHARP -> 1
                ThemeBorderStyle.HIGH_CONTRAST -> 2
            }
            KamelotBorderStyle.SOFT -> 0
            KamelotBorderStyle.SHARP -> 1
            KamelotBorderStyle.HIGH_CONTRAST -> 2
        }

        val resolvedBackground = when (appearance.keyBackgroundStyle) {
            KamelotKeyBackgroundStyle.MATCH_PRESET -> preset.backgroundStyle
            KamelotKeyBackgroundStyle.SOLID -> ThemeBackgroundStyle.SOLID
            KamelotKeyBackgroundStyle.ELEVATED -> ThemeBackgroundStyle.CONTRAST
            KamelotKeyBackgroundStyle.GLASS -> ThemeBackgroundStyle.GLASS
        }

        val resolvedGlow = when (appearance.glowIntensity) {
            KamelotGlowIntensity.OFF -> 0
            KamelotGlowIntensity.SUBTLE -> 1
            KamelotGlowIntensity.VIVID -> 2
        }

        val neutralStroke = if (isDark) Color.argb(70, 255, 255, 255) else Color.argb(40, 0, 0, 0)
        val contrastStroke = if (isDark) Color.argb(170, 255, 255, 255) else Color.argb(120, 0, 0, 0)
        val glassStroke = ColorUtils.setAlphaComponent(accent, if (resolvedGlow == 2) 170 else 110)
        val stroke = when (resolvedBorder) {
            2 -> contrastStroke
            1 -> glassStroke
            else -> neutralStroke
        }

        val fill = when (resolvedBackground) {
            ThemeBackgroundStyle.SOLID -> if (isDark) Color.argb(18, 255, 255, 255) else Color.argb(10, 0, 0, 0)
            ThemeBackgroundStyle.CONTRAST -> if (isDark) Color.argb(26, 255, 255, 255) else Color.argb(16, 0, 0, 0)
            ThemeBackgroundStyle.GLASS -> ColorUtils.setAlphaComponent(accent, if (isDark) 32 else 24)
        }
        val pressedFill = when (resolvedBackground) {
            ThemeBackgroundStyle.SOLID -> if (isDark) Color.argb(34, 255, 255, 255) else Color.argb(22, 0, 0, 0)
            ThemeBackgroundStyle.CONTRAST -> if (isDark) Color.argb(52, 255, 255, 255) else Color.argb(34, 0, 0, 0)
            ThemeBackgroundStyle.GLASS -> ColorUtils.setAlphaComponent(accent, if (resolvedGlow == 2) 88 else 64)
        }
        val pressedStroke = ColorUtils.blendARGB(stroke, accent, 0.35f)

        val glowColor = if (resolvedGlow == 0) Color.TRANSPARENT
        else ColorUtils.setAlphaComponent(accent, if (resolvedGlow == 2) 130 else 70)
        val glowRadius = when (resolvedGlow) {
            0 -> 0f
            1 -> 6f * density
            else -> 12f * density
        }

        return KamelotVisualStyle(
            keyCornerRadius = resolvedCorner,
            outlineWidth = when (resolvedBorder) {
                2 -> 1.45f * density
                1 -> 1.15f * density
                else -> 0.85f * density
            },
            fillColor = fill,
            pressedFillColor = pressedFill,
            strokeColor = stroke,
            pressedStrokeColor = pressedStroke,
            accentColor = accent,
            glowColor = glowColor,
            glowRadius = glowRadius,
        )
    }
}
