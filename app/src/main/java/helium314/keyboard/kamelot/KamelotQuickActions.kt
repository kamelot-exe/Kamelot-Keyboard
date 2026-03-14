// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import kotlinx.serialization.Serializable

@Serializable
enum class QuickActionVisibilityRule {
    ALWAYS,
    EXPERIMENTAL_ONLY,
    HIDDEN,
}

@Serializable
enum class QuickActionProfileScope {
    ACTIVE_PROFILE,
    ALL_PROFILES,
}

@Serializable
enum class KamelotStripMode {
    PROFILE_DRIVEN,
    QUICK_ACTIONS_ONLY,
    MACROS_ONLY,
    MINIMAL,
}

@Serializable
enum class KamelotStripPreset {
    BALANCED,
    COMMUNICATION,
    PRODUCTIVITY,
    COMPACT,
}

@Serializable
enum class KamelotQuickPanelPreset {
    BALANCED,
    FOCUSED,
    SWITCHER,
    MINIMAL,
}

@Serializable
enum class KamelotStripBehavior {
    ONLY_WHEN_IDLE,
    ALONGSIDE_SUGGESTIONS,
    HIDDEN,
}

@Serializable
enum class KamelotContextCategory {
    GENERAL,
    CHAT,
    WORK,
    BROWSING,
    NOTES,
    CODING,
}

@Serializable
enum class KamelotAppCategoryIntent {
    CHAT,
    WORK,
    BROWSER,
    NOTES,
    CODING,
}

@Serializable
data class KamelotProfileRoutingIntent(
    val appCategoryIntent: KamelotAppCategoryIntent? = null,
    val manualOnly: Boolean = true,
)

@Serializable
data class QuickActionItem(
    val id: String,
    val label: String,
    val action: KeyboardAction,
    val iconToken: String? = null,
    val visibility: QuickActionVisibilityRule = QuickActionVisibilityRule.ALWAYS,
    val profileScope: QuickActionProfileScope = QuickActionProfileScope.ACTIVE_PROFILE,
    val enabled: Boolean = true,
    val order: Int = 0,
)

@Serializable
data class MacroStripItem(
    val macroId: String,
    val labelOverride: String? = null,
    val iconToken: String? = null,
    val visibility: QuickActionVisibilityRule = QuickActionVisibilityRule.ALWAYS,
    val enabled: Boolean = true,
    val order: Int = 0,
)

@Serializable
data class QuickActionsConfig(
    val stripMode: KamelotStripMode = KamelotStripMode.PROFILE_DRIVEN,
    val stripPreset: KamelotStripPreset = KamelotStripPreset.BALANCED,
    val quickPanelPreset: KamelotQuickPanelPreset = KamelotQuickPanelPreset.BALANCED,
    val stripBehavior: KamelotStripBehavior = KamelotStripBehavior.ONLY_WHEN_IDLE,
    val macroStripEnabled: Boolean = true,
    val showProfileSwitcher: Boolean = true,
    val showProfileBadge: Boolean = true,
    val maxVisibleItems: Int = 6,
    val visibleQuickActions: List<QuickActionItem> = emptyList(),
    val visibleMacros: List<MacroStripItem> = emptyList(),
)

enum class KamelotStripItemSource {
    QUICK_ACTION,
    MACRO,
    PROFILE,
}

enum class KamelotStripItemRole {
    PRIMARY_ACTION,
    MACRO,
    PROFILE_BADGE,
    PROFILE_SWITCH,
}

data class KamelotStripItem(
    val stableId: String,
    val label: String,
    val action: KeyboardAction,
    val iconToken: String? = null,
    val source: KamelotStripItemSource,
    val role: KamelotStripItemRole,
    val order: Int,
    val emphasized: Boolean = false,
)

val KamelotStripPreset.displayName: String
    get() = when (this) {
        KamelotStripPreset.BALANCED -> "Balanced"
        KamelotStripPreset.COMMUNICATION -> "Communication"
        KamelotStripPreset.PRODUCTIVITY -> "Productivity"
        KamelotStripPreset.COMPACT -> "Compact"
    }

val KamelotStripMode.displayName: String
    get() = when (this) {
        KamelotStripMode.PROFILE_DRIVEN -> "Profile driven"
        KamelotStripMode.QUICK_ACTIONS_ONLY -> "Quick actions only"
        KamelotStripMode.MACROS_ONLY -> "Macros only"
        KamelotStripMode.MINIMAL -> "Minimal"
    }

val KamelotQuickPanelPreset.displayName: String
    get() = when (this) {
        KamelotQuickPanelPreset.BALANCED -> "Balanced"
        KamelotQuickPanelPreset.FOCUSED -> "Focused"
        KamelotQuickPanelPreset.SWITCHER -> "Switcher"
        KamelotQuickPanelPreset.MINIMAL -> "Minimal"
    }

val KamelotStripBehavior.displayName: String
    get() = when (this) {
        KamelotStripBehavior.ONLY_WHEN_IDLE -> "Only when idle"
        KamelotStripBehavior.ALONGSIDE_SUGGESTIONS -> "Alongside suggestions"
        KamelotStripBehavior.HIDDEN -> "Hidden"
    }
