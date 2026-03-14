// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot

import android.content.SharedPreferences

object KamelotQuickActionsResolver {
    fun resolve(
        profile: KamelotProfile,
        prefs: SharedPreferences,
        availableProfiles: List<KamelotProfile> = listOf(profile),
    ): List<KamelotStripItem> {
        val config = profile.quickActionsConfig
        if (config.stripBehavior == KamelotStripBehavior.HIDDEN) return emptyList()
        val quickActions = config.visibleQuickActions
            .filter { it.enabled && isVisible(it.visibility, prefs) }
            .sortedBy { it.order }
            .map {
                KamelotStripItem(
                    stableId = "action:${it.id}",
                    label = it.label,
                    action = it.action,
                    iconToken = it.iconToken,
                    source = KamelotStripItemSource.QUICK_ACTION,
                    role = KamelotStripItemRole.PRIMARY_ACTION,
                    order = it.order,
                )
            }
        val macrosById = profile.macros.associateBy { it.id }
        val macros = if (config.macroStripEnabled) config.visibleMacros else emptyList()
        val macroItems = macros
            .filter { it.enabled && isVisible(it.visibility, prefs) }
            .sortedBy { it.order }
            .mapNotNull { item ->
                val macro = macrosById[item.macroId] ?: return@mapNotNull null
                KamelotStripItem(
                    stableId = "macro:${macro.id}",
                    label = item.labelOverride ?: macro.label,
                    action = KeyboardAction(KeyboardActionType.RUN_MACRO, macro.id),
                    iconToken = item.iconToken,
                    source = KamelotStripItemSource.MACRO,
                    role = KamelotStripItemRole.MACRO,
                    order = item.order,
                )
            }
        val profileItems = buildProfileItems(profile, availableProfiles, config.maxVisibleItems)
        val contentItems = when (config.stripMode) {
            KamelotStripMode.PROFILE_DRIVEN -> mergeByPreset(config.quickPanelPreset, quickActions, macroItems)
            KamelotStripMode.QUICK_ACTIONS_ONLY -> quickActions
            KamelotStripMode.MACROS_ONLY -> macroItems
            KamelotStripMode.MINIMAL -> emptyList()
        }
        val merged = when (config.quickPanelPreset) {
            KamelotQuickPanelPreset.BALANCED -> profileItems.take(2) + contentItems
            KamelotQuickPanelPreset.FOCUSED -> profileItems.take(1) + contentItems
            KamelotQuickPanelPreset.SWITCHER -> profileItems + contentItems.take(2)
            KamelotQuickPanelPreset.MINIMAL -> profileItems.take(1) + contentItems.take(1)
        }
        return merged
            .take(config.maxVisibleItems)
            .mapIndexed { index, item -> item.copy(order = index) }
    }

    private fun mergeByPreset(
        preset: KamelotQuickPanelPreset,
        quickActions: List<KamelotStripItem>,
        macros: List<KamelotStripItem>,
    ): List<KamelotStripItem> = when (preset) {
        KamelotQuickPanelPreset.BALANCED -> interleave(quickActions, macros)
        KamelotQuickPanelPreset.FOCUSED -> quickActions + macros
        KamelotQuickPanelPreset.SWITCHER -> quickActions + macros
        KamelotQuickPanelPreset.MINIMAL -> (quickActions + macros).take(2)
    }

    private fun buildProfileItems(
        activeProfile: KamelotProfile,
        availableProfiles: List<KamelotProfile>,
        maxVisibleItems: Int,
    ): List<KamelotStripItem> {
        val config = activeProfile.quickActionsConfig
        if (!config.showProfileBadge && !config.showProfileSwitcher) return emptyList()
        val otherProfiles = availableProfiles.filterNot { it.id == activeProfile.id }
        val nextProfile = otherProfiles.firstOrNull() ?: activeProfile
        val items = mutableListOf<KamelotStripItem>()
        if (config.showProfileBadge) {
            items += KamelotStripItem(
                stableId = "profile:badge:${activeProfile.id}",
                label = activeProfile.name,
                action = KeyboardAction(KeyboardActionType.SWITCH_PROFILE, nextProfile.id),
                source = KamelotStripItemSource.PROFILE,
                role = KamelotStripItemRole.PROFILE_BADGE,
                order = -2,
                emphasized = true,
            )
        }
        if (config.showProfileSwitcher) {
            otherProfiles.take(maxVisibleItems.coerceAtLeast(2)).forEachIndexed { index, profile ->
                items += KamelotStripItem(
                    stableId = "profile:switch:${profile.id}",
                    label = profile.name,
                    action = KeyboardAction(KeyboardActionType.SWITCH_PROFILE, profile.id),
                    source = KamelotStripItemSource.PROFILE,
                    role = KamelotStripItemRole.PROFILE_SWITCH,
                    order = -1 + index,
                )
            }
        }
        return items
    }

    private fun interleave(
        first: List<KamelotStripItem>,
        second: List<KamelotStripItem>,
    ): List<KamelotStripItem> {
        if (first.isEmpty()) return second
        if (second.isEmpty()) return first
        val merged = mutableListOf<KamelotStripItem>()
        val max = maxOf(first.size, second.size)
        for (index in 0 until max) {
            first.getOrNull(index)?.let(merged::add)
            second.getOrNull(index)?.let(merged::add)
        }
        return merged
    }

    private fun isVisible(rule: QuickActionVisibilityRule, prefs: SharedPreferences): Boolean = when (rule) {
        QuickActionVisibilityRule.ALWAYS -> true
        QuickActionVisibilityRule.EXPERIMENTAL_ONLY ->
            KamelotFeatureFlags.isExperimentalFeaturesEnabled(prefs)
        QuickActionVisibilityRule.HIDDEN -> false
    }
}
