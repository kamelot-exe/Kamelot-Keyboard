// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.actions

sealed interface KamelotActionResult {
    data class Success(val message: String) : KamelotActionResult
    data class Ignored(val reason: String) : KamelotActionResult
    data class Unsupported(val reason: String) : KamelotActionResult
    data class FailedSafely(val reason: String) : KamelotActionResult
}
