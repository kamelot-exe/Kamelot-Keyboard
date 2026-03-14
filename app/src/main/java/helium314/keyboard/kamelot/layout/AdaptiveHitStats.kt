// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import kotlinx.serialization.Serializable

@Serializable
data class AdaptiveHitStats(
    val keyId: String,
    val averageOffsetX: Float = 0f,
    val averageOffsetY: Float = 0f,
    val sampleCount: Int = 0,
    val averageDistanceSquared: Float = 0f,
) {
    fun record(offsetX: Float, offsetY: Float): AdaptiveHitStats {
        val nextCount = sampleCount + 1
        val nextAverageOffsetX = averageOffsetX + ((offsetX - averageOffsetX) / nextCount)
        val nextAverageOffsetY = averageOffsetY + ((offsetY - averageOffsetY) / nextCount)
        val distanceSquared = (offsetX * offsetX) + (offsetY * offsetY)
        val nextAverageDistanceSquared = averageDistanceSquared + ((distanceSquared - averageDistanceSquared) / nextCount)
        return copy(
            averageOffsetX = nextAverageOffsetX,
            averageOffsetY = nextAverageOffsetY,
            sampleCount = nextCount,
            averageDistanceSquared = nextAverageDistanceSquared,
        )
    }
}
