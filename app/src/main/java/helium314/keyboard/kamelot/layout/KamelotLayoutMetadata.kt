// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import helium314.keyboard.kamelot.FutureLayoutMode
import kotlinx.serialization.Serializable

@Serializable
data class KamelotLayoutMetadata(
    val layoutMode: FutureLayoutMode = FutureLayoutMode.STANDARD,
    val hexRadius: Float? = null,
    val hexSpacing: Float? = null,
    val rowOffset: Float? = null,
) {
    companion object {
        @JvmStatic
        fun standard() = KamelotLayoutMetadata()
    }
}
