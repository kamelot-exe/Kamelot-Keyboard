// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import helium314.keyboard.keyboard.Key

class HexSwipePointSnapper(
    private val gridLayout: HexGridLayout,
) {
    data class SnappedPoint(
        val x: Int,
        val y: Int,
        val key: Key?,
        val centerX: Float,
        val centerY: Float,
    )

    fun snap(rawX: Int, rawY: Int, lastKeyId: String? = null): SnappedPoint {
        val touchX = rawX.toFloat()
        val touchY = rawY.toFloat()
        var bestGeometry: HexKeyGeometry? = null
        var bestScore = Float.MAX_VALUE
        for (geometry in gridLayout.allGeometries()) {
            val distanceScore = geometry.distanceSquaredToCenter(touchX, touchY)
            val sameKeyBonus = if ("${geometry.key.code}@${geometry.key.x},${geometry.key.y}" == lastKeyId) -geometry.radius else 0f
            val score = distanceScore + sameKeyBonus
            if (score < bestScore) {
                bestScore = score
                bestGeometry = geometry
            }
        }
        val geometry = bestGeometry ?: return SnappedPoint(rawX, rawY, null, touchX, touchY)
        val maxSnapDistance = geometry.radius * 1.35f
        val withinSnapDistance = bestScore <= maxSnapDistance * maxSnapDistance
        if (!withinSnapDistance) {
            return SnappedPoint(rawX, rawY, null, touchX, touchY)
        }
        val snapStrength = if (geometry.contains(touchX, touchY, 1.02f)) 0.35f else 0.55f
        val snappedX = rawX + ((geometry.centerX - touchX) * snapStrength).toInt()
        val snappedY = rawY + ((geometry.centerY - touchY) * snapStrength).toInt()
        return SnappedPoint(
            x = snappedX,
            y = snappedY,
            key = geometry.key,
            centerX = geometry.centerX,
            centerY = geometry.centerY,
        )
    }
}
