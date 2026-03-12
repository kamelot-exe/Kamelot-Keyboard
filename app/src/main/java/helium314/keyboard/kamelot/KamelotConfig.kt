// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import kotlinx.serialization.Serializable

object KamelotConfig {
    const val PRODUCT_NAME = "Kamelot Keyboard"
    const val PRODUCT_TAGLINE = "A private, customizable Android keyboard that adapts to the user."
    const val FOUNDATION_PHASE = "Phase 2"
    val supportedLayoutModes = FutureLayoutMode.entries
}

@Serializable
enum class FutureLayoutMode {
    STANDARD,
    HEX_EXPERIMENTAL,
}
