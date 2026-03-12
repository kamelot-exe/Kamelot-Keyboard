// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import kotlinx.serialization.Serializable

@Serializable
data class ThemePreset(
    val id: String,
    val displayName: String,
    val baseThemeId: String,
    val accentColor: String?,
    val backgroundStyle: ThemeBackgroundStyle,
    val keyShape: ThemeKeyShape,
    val dayThemeName: String,
    val nightThemeName: String,
    val useKeyBorders: Boolean = false,
)

@Serializable
enum class ThemeBackgroundStyle {
    SOLID,
    CONTRAST,
    GLASS,
}

@Serializable
enum class ThemeKeyShape {
    SYSTEM,
    ROUNDED,
    SHARP,
}
