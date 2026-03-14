// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

data class HexSwipeTrailModel(
    val lastCenterX: Float? = null,
    val lastCenterY: Float? = null,
    val lastKeyId: String? = null,
    val stableSteps: Int = 0,
) {
    fun advance(snappedCenterX: Float, snappedCenterY: Float, snappedKeyId: String?): HexSwipeTrailModel {
        val isSameKey = snappedKeyId != null && snappedKeyId == lastKeyId
        return copy(
            lastCenterX = snappedCenterX,
            lastCenterY = snappedCenterY,
            lastKeyId = snappedKeyId,
            stableSteps = if (isSameKey) stableSteps + 1 else 1,
        )
    }
}
