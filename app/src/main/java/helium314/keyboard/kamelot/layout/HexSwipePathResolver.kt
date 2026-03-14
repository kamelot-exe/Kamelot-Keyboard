// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import helium314.keyboard.keyboard.Key

class HexSwipePathResolver(
    private val snapper: HexSwipePointSnapper,
) {
    private var trailModel = HexSwipeTrailModel()

    data class ResolvedPoint(
        val x: Int,
        val y: Int,
        val key: Key?,
    )

    fun reset() {
        trailModel = HexSwipeTrailModel()
    }

    fun resolve(rawX: Int, rawY: Int): ResolvedPoint {
        val snapped = snapper.snap(rawX, rawY, trailModel.lastKeyId)
        val snappedKey = snapped.key
        val snappedKeyId = snappedKey?.let { "${it.code}@${it.x},${it.y}" }
        val candidate = if (shouldSuppressOscillation(snappedKeyId)) {
            ResolvedPoint(rawX, rawY, null)
        } else {
            ResolvedPoint(snapped.x, snapped.y, snapped.key)
        }
        trailModel = trailModel.advance(
            snappedCenterX = snapped.centerX,
            snappedCenterY = snapped.centerY,
            snappedKeyId = snappedKeyId,
        )
        return candidate
    }

    private fun shouldSuppressOscillation(nextKeyId: String?): Boolean {
        val lastKeyId = trailModel.lastKeyId ?: return false
        if (nextKeyId == null || nextKeyId == lastKeyId) return false
        return trailModel.stableSteps <= 1
    }
}
