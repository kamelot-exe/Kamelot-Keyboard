// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import helium314.keyboard.keyboard.Key
import helium314.keyboard.keyboard.Keyboard
import kotlin.math.min
import kotlin.math.sqrt

class HexGridLayout private constructor(
    private val geometryByKey: Map<Key, HexKeyGeometry>,
    val metadata: KamelotLayoutMetadata,
) {
    fun getGeometry(key: Key): HexKeyGeometry? = geometryByKey[key]

    fun allGeometries(): Collection<HexKeyGeometry> = geometryByKey.values

    companion object {
        private const val DEFAULT_HEX_SPACING = 0.94f
        private const val DEFAULT_ROW_OFFSET = 0.5f

        @JvmStatic
        fun fromKeyboard(
            keyboard: Keyboard,
            metadata: KamelotLayoutMetadata = KamelotLayoutMetadata.standard(),
        ): HexGridLayout {
            val groupedRows = keyboard.sortedKeys
                .filterNot { it.isSpacer }
                .groupBy { it.y }
                .toSortedMap()

            val hexSpacing = metadata.hexSpacing ?: DEFAULT_HEX_SPACING
            val rowOffsetFactor = metadata.rowOffset ?: DEFAULT_ROW_OFFSET
            val keyboardWidth = keyboard.mOccupiedWidth.toFloat()
            val geometryByKey = LinkedHashMap<Key, HexKeyGeometry>()

            groupedRows.values.forEachIndexed { rowIndex, rowKeys ->
                rowKeys.forEach { key ->
                    val drawWidth = key.drawWidth.toFloat()
                    val keyHeight = key.height.toFloat()
                    val maxRadiusByWidth = drawWidth * 0.5f
                    val maxRadiusByHeight = keyHeight / sqrt(3f)
                    val radius = ((metadata.hexRadius ?: min(maxRadiusByWidth, maxRadiusByHeight)) * hexSpacing)
                        .coerceAtLeast(8f)
                    val rawOffset = if (rowIndex % 2 == 0) 0f else drawWidth * rowOffsetFactor
                    val centerX = (key.drawX + drawWidth * 0.5f + rawOffset)
                        .coerceIn(radius, keyboardWidth - radius)
                    val centerY = key.y + (keyHeight * 0.5f)
                    geometryByKey[key] = HexKeyGeometry(
                        key = key,
                        centerX = centerX,
                        centerY = centerY,
                        radius = radius,
                    )
                }
            }
            return HexGridLayout(geometryByKey, metadata)
        }
    }
}
