// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class SettingsAccent(
    val container: Color,
    val onContainer: Color,
    val subtleContainer: Color,
    val subtleOnContainer: Color,
)

@Composable
fun settingsAccent(index: Int): SettingsAccent {
    val colors = MaterialTheme.colorScheme
    return when (index % 6) {
        0 -> colors.accent(colors.primaryContainer, colors.onPrimaryContainer)
        1 -> colors.accent(colors.secondaryContainer, colors.onSecondaryContainer)
        2 -> colors.accent(colors.tertiaryContainer, colors.onTertiaryContainer)
        3 -> colors.accent(colors.errorContainer, colors.onErrorContainer)
        4 -> colors.accent(colors.surfaceVariant, colors.onSurfaceVariant)
        else -> colors.accent(colors.inversePrimary.copy(alpha = 0.28f), colors.onSurface)
    }
}

private fun ColorScheme.accent(container: Color, onContainer: Color) = SettingsAccent(
    container = container,
    onContainer = onContainer,
    subtleContainer = container.copy(alpha = 0.38f),
    subtleOnContainer = onContainer,
)
