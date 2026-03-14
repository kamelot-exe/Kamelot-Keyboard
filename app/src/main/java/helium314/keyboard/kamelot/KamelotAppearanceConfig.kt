// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import kotlinx.serialization.Serializable

@Serializable
data class KamelotAppearanceConfig(
    val accentPalette: KamelotAccentPalette = KamelotAccentPalette.AUTO,
    val keyBackgroundStyle: KamelotKeyBackgroundStyle = KamelotKeyBackgroundStyle.MATCH_PRESET,
    val borderStyle: KamelotBorderStyle = KamelotBorderStyle.MATCH_PRESET,
    val cornerStyle: KamelotCornerStyle = KamelotCornerStyle.MATCH_PRESET,
    val glowIntensity: KamelotGlowIntensity = KamelotGlowIntensity.SUBTLE,
    val textScale: KamelotTextScale = KamelotTextScale.NORMAL,
    val spacingDensity: KamelotSpacingDensity = KamelotSpacingDensity.BALANCED,
)

@Serializable
enum class KamelotAccentPalette {
    AUTO,
    NEUTRAL,
    OCEAN,
    NEON,
    HIGH_CONTRAST,
}

@Serializable
enum class KamelotKeyBackgroundStyle {
    MATCH_PRESET,
    SOLID,
    ELEVATED,
    GLASS,
}

@Serializable
enum class KamelotBorderStyle {
    MATCH_PRESET,
    SOFT,
    SHARP,
    HIGH_CONTRAST,
}

@Serializable
enum class KamelotCornerStyle {
    MATCH_PRESET,
    SYSTEM,
    ROUNDED,
    SHARP,
}

@Serializable
enum class KamelotGlowIntensity {
    OFF,
    SUBTLE,
    VIVID,
}

@Serializable
enum class KamelotTextScale {
    COMPACT,
    NORMAL,
    LARGE,
}

@Serializable
enum class KamelotSpacingDensity {
    COMPACT,
    BALANCED,
    COMFORTABLE,
}

fun appearanceConfigForPreset(preset: ThemePreset) = when (preset.id) {
    "brutal_black" -> KamelotAppearanceConfig(
        accentPalette = KamelotAccentPalette.NEUTRAL,
        keyBackgroundStyle = KamelotKeyBackgroundStyle.SOLID,
        borderStyle = KamelotBorderStyle.SHARP,
        cornerStyle = KamelotCornerStyle.SHARP,
        glowIntensity = KamelotGlowIntensity.OFF,
        spacingDensity = KamelotSpacingDensity.COMPACT,
    )
    "glass_neon" -> KamelotAppearanceConfig(
        accentPalette = KamelotAccentPalette.NEON,
        keyBackgroundStyle = KamelotKeyBackgroundStyle.GLASS,
        borderStyle = KamelotBorderStyle.SOFT,
        cornerStyle = KamelotCornerStyle.ROUNDED,
        glowIntensity = KamelotGlowIntensity.VIVID,
        spacingDensity = KamelotSpacingDensity.COMFORTABLE,
    )
    "high_contrast" -> KamelotAppearanceConfig(
        accentPalette = KamelotAccentPalette.HIGH_CONTRAST,
        keyBackgroundStyle = KamelotKeyBackgroundStyle.ELEVATED,
        borderStyle = KamelotBorderStyle.HIGH_CONTRAST,
        cornerStyle = KamelotCornerStyle.SHARP,
        glowIntensity = KamelotGlowIntensity.SUBTLE,
    )
    "minimal_dark" -> KamelotAppearanceConfig(
        accentPalette = KamelotAccentPalette.NEUTRAL,
        keyBackgroundStyle = KamelotKeyBackgroundStyle.SOLID,
        borderStyle = KamelotBorderStyle.SOFT,
        cornerStyle = KamelotCornerStyle.SYSTEM,
        glowIntensity = KamelotGlowIntensity.OFF,
        spacingDensity = KamelotSpacingDensity.BALANCED,
    )
    else -> KamelotAppearanceConfig()
}
