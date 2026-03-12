// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import kotlinx.serialization.Serializable

@Serializable
data class KamelotMacro(
    val id: String,
    val label: String,
    val textPayload: String,
)
