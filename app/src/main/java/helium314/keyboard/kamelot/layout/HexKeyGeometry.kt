// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import android.graphics.Path
import android.graphics.RectF
import helium314.keyboard.keyboard.Key
import kotlin.math.cos
import kotlin.math.sin

class HexKeyGeometry(
    val key: Key,
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
) {
    private val absoluteVertices: FloatArray = buildVertices(centerX, centerY, radius)
    private val localVertices: FloatArray = absoluteVertices.copyOf().also { vertices ->
        val originX = key.drawX.toFloat()
        val originY = key.y.toFloat()
        var index = 0
        while (index < vertices.size) {
            vertices[index] -= originX
            vertices[index + 1] -= originY
            index += 2
        }
    }

    val path: Path by lazy(LazyThreadSafetyMode.NONE) {
        Path().apply {
            moveTo(localVertices[0], localVertices[1])
            for (index in 2 until localVertices.size step 2) {
                lineTo(localVertices[index], localVertices[index + 1])
            }
            close()
        }
    }

    val bounds: RectF by lazy(LazyThreadSafetyMode.NONE) {
        RectF().apply { path.computeBounds(this, true) }
    }

    fun contains(x: Float, y: Float, toleranceMultiplier: Float = 1.0f): Boolean {
        if (toleranceMultiplier <= 1.0f) {
            return pointInPolygon(x, y, absoluteVertices)
        }
        val expanded = FloatArray(absoluteVertices.size)
        var index = 0
        while (index < absoluteVertices.size) {
            expanded[index] = centerX + (absoluteVertices[index] - centerX) * toleranceMultiplier
            expanded[index + 1] = centerY + (absoluteVertices[index + 1] - centerY) * toleranceMultiplier
            index += 2
        }
        return pointInPolygon(x, y, expanded)
    }

    fun distanceSquaredToCenter(x: Float, y: Float): Float {
        val dx = centerX - x
        val dy = centerY - y
        return dx * dx + dy * dy
    }

    private fun buildVertices(centerX: Float, centerY: Float, radius: Float): FloatArray {
        val vertices = FloatArray(12)
        var vertex = 0
        for (side in 0 until 6) {
            val angleRadians = Math.toRadians((60.0 * side) - 90.0)
            vertices[vertex] = centerX + (radius * cos(angleRadians)).toFloat()
            vertices[vertex + 1] = centerY + (radius * sin(angleRadians)).toFloat()
            vertex += 2
        }
        return vertices
    }

    private fun pointInPolygon(x: Float, y: Float, vertices: FloatArray): Boolean {
        var inside = false
        var i = 0
        var j = vertices.size - 2
        while (i < vertices.size) {
            val xi = vertices[i]
            val yi = vertices[i + 1]
            val xj = vertices[j]
            val yj = vertices[j + 1]
            val intersects = ((yi > y) != (yj > y)) &&
                (x < (xj - xi) * (y - yi) / ((yj - yi).takeIf { it != 0f } ?: 0.0001f) + xi)
            if (intersects) inside = !inside
            j = i
            i += 2
        }
        return inside
    }
}
