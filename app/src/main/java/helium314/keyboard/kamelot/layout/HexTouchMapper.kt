// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import helium314.keyboard.keyboard.Key

open class HexTouchMapper @JvmOverloads constructor(
    protected val gridLayout: HexGridLayout,
    protected val toleranceMultiplier: Float = 1.08f,
) {
    open fun detectHitKey(x: Int, y: Int): Key? {
        val touchX = x.toFloat()
        val touchY = y.toFloat()
        var bestKey: Key? = null
        var bestDistance = Float.MAX_VALUE
        for (geometry in gridLayout.allGeometries()) {
            if (!geometry.contains(touchX, touchY, toleranceMultiplier)) continue
            val distance = geometry.distanceSquaredToCenter(touchX, touchY)
            if (distance <= bestDistance) {
                bestDistance = distance
                bestKey = geometry.key
            }
        }
        return bestKey
    }

    fun getGeometry(key: Key): HexKeyGeometry? = gridLayout.getGeometry(key)
}
