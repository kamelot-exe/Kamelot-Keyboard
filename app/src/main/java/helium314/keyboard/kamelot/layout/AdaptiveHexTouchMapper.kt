// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import helium314.keyboard.keyboard.Key

class AdaptiveHexTouchMapper @JvmOverloads constructor(
    gridLayout: HexGridLayout,
    private val adaptiveHitModel: AdaptiveHitModel,
    private val baseToleranceMultiplier: Float = 1.08f,
) : HexTouchMapper(gridLayout, baseToleranceMultiplier) {
    override fun detectHitKey(x: Int, y: Int): Key? {
        val touchX = x.toFloat()
        val touchY = y.toFloat()
        var bestKey: Key? = null
        var bestDistance = Float.MAX_VALUE
        for (geometry in gridLayout.allGeometries()) {
            val key = geometry.key
            val (biasX, biasY) = adaptiveHitModel.getBiasFor(key, geometry)
            val toleranceMultiplier = baseToleranceMultiplier * adaptiveHitModel.getToleranceMultiplier(key, geometry)
            if (!geometry.contains(touchX - biasX, touchY - biasY, toleranceMultiplier)) continue
            val distance = geometry.distanceSquaredToCenter(touchX - biasX, touchY - biasY)
            if (distance <= bestDistance) {
                bestDistance = distance
                bestKey = key
            }
        }
        return bestKey ?: super.detectHitKey(x, y)
    }

    fun recordSuccessfulTouch(key: Key, x: Int, y: Int) {
        val geometry = getGeometry(key) ?: return
        adaptiveHitModel.recordSuccessfulTouch(key, geometry, x.toFloat(), y.toFloat())
    }
}
