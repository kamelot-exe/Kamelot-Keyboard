// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import helium314.keyboard.latin.common.LocaleUtils.constructLocale
import helium314.keyboard.latin.settings.SettingsSubtype.Companion.toSettingsSubtype
import helium314.keyboard.latin.settings.getTransitionAnimationScale
import helium314.keyboard.settings.screens.AboutScreen
import helium314.keyboard.settings.screens.AdvancedSettingsScreen
import helium314.keyboard.settings.screens.AppearanceScreen
import helium314.keyboard.settings.screens.ColorsScreen
import helium314.keyboard.settings.screens.DebugScreen
import helium314.keyboard.settings.screens.DictionaryScreen
import helium314.keyboard.settings.screens.GestureTypingScreen
import helium314.keyboard.settings.screens.KamelotGestureOsScreen
import helium314.keyboard.settings.screens.KamelotExperimentsScreen
import helium314.keyboard.settings.screens.KamelotModulesScreen
import helium314.keyboard.settings.screens.KamelotScreen
import helium314.keyboard.settings.screens.KamelotMacrosScreen
import helium314.keyboard.settings.screens.KamelotProfilesScreen
import helium314.keyboard.settings.screens.KamelotQuickActionsScreen
import helium314.keyboard.settings.screens.KamelotThemesScreen
import helium314.keyboard.settings.screens.LanguageScreen
import helium314.keyboard.settings.screens.MainSettingsScreen
import helium314.keyboard.settings.screens.ExperimentalHubScreen
import helium314.keyboard.settings.screens.GesturesHubScreen
import helium314.keyboard.settings.screens.LanguagesHubScreen
import helium314.keyboard.settings.screens.LayoutTypingHubScreen
import helium314.keyboard.settings.screens.ActionsToolbarHubScreen
import helium314.keyboard.settings.screens.AppearanceHubScreen
import helium314.keyboard.settings.screens.MacrosClipboardHubScreen
import helium314.keyboard.settings.screens.AboutAdvancedHubScreen
import helium314.keyboard.settings.screens.PrivacyDataHubScreen
import helium314.keyboard.settings.screens.PersonalDictionariesScreen
import helium314.keyboard.settings.screens.PersonalDictionaryScreen
import helium314.keyboard.settings.screens.PreferencesScreen
import helium314.keyboard.settings.screens.ProfilesHubScreen
import helium314.keyboard.settings.screens.SecondaryLayoutScreen
import helium314.keyboard.settings.screens.SubtypeScreen
import helium314.keyboard.settings.screens.TextCorrectionScreen
import helium314.keyboard.settings.screens.ToolbarScreen
import helium314.keyboard.settings.screens.gesturedata.GestureDataScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun SettingsNavHost(
    onClickBack: () -> Unit,
    startDestination: String? = null,
) {
    val navController = rememberNavController()
    val dir = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1 else -1
    val target = SettingsDestination.navTarget.collectAsState()

    // duration does not change when system setting changes, but that's rare enough to not care
    val duration = (250 * getTransitionAnimationScale(LocalContext.current)).toInt()
    val animation = tween<IntOffset>(durationMillis = duration)

    fun goBack() {
        if (!navController.popBackStack()) onClickBack()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination ?: SettingsDestination.Settings,
        enterTransition = { slideInHorizontally(initialOffsetX = { +it * dir }, animationSpec = animation) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it * dir }, animationSpec = animation) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it * dir }, animationSpec = animation) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { +it * dir }, animationSpec = animation) }
    ) {
        composable(SettingsDestination.Settings) {
            MainSettingsScreen(
                onClickAppearance = { navController.navigate(SettingsDestination.AppearanceHub) },
                onClickLayoutTyping = { navController.navigate(SettingsDestination.LayoutTypingHub) },
                onClickProfiles = { navController.navigate(SettingsDestination.ProfilesHub) },
                onClickGestures = { navController.navigate(SettingsDestination.GesturesHub) },
                onClickActionsToolbar = { navController.navigate(SettingsDestination.ActionsToolbarHub) },
                onClickMacrosClipboard = { navController.navigate(SettingsDestination.MacrosClipboardHub) },
                onClickLanguages = { navController.navigate(SettingsDestination.LanguagesHub) },
                onClickPrivacyData = { navController.navigate(SettingsDestination.PrivacyDataHub) },
                onClickExperimental = { navController.navigate(SettingsDestination.ExperimentalHub) },
                onClickAboutAdvanced = { navController.navigate(SettingsDestination.AboutAdvancedHub) },
                onClickBack = ::goBack,
            )
        }
        composable(SettingsDestination.AppearanceHub) {
            AppearanceHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.LayoutTypingHub) {
            LayoutTypingHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.ProfilesHub) {
            ProfilesHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.GesturesHub) {
            GesturesHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.ActionsToolbarHub) {
            ActionsToolbarHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.MacrosClipboardHub) {
            MacrosClipboardHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.LanguagesHub) {
            LanguagesHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.PrivacyDataHub) {
            PrivacyDataHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.ExperimentalHub) {
            ExperimentalHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.AboutAdvancedHub) {
            AboutAdvancedHubScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.About) {
            AboutScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.TextCorrection) {
            TextCorrectionScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Preferences) {
            PreferencesScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Toolbar) {
            ToolbarScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.GestureTyping) {
            GestureTypingScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.DataGathering) {
            GestureDataScreen(onClickBack = ::goBack)
        }
/*      will be added as part of passive data gathering
        composable(SettingsDestination.DataReview) {
            ReviewScreen(onClickBack = ::goBack)
        }*/
        composable(SettingsDestination.Advanced) {
            AdvancedSettingsScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Debug) {
            DebugScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Appearance) {
            AppearanceScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Kamelot) {
            KamelotScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.KamelotProfiles) {
            KamelotProfilesScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.KamelotThemes) {
            KamelotThemesScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.KamelotModules) {
            KamelotModulesScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.KamelotExperiments) {
            KamelotExperimentsScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.KamelotGestureOs) {
            KamelotGestureOsScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.KamelotQuickActions) {
            KamelotQuickActionsScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.KamelotMacros) {
            KamelotMacrosScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.PersonalDictionary + "{locale}") {
            val locale = it.arguments?.getString("locale")?.takeIf { loc -> loc.isNotBlank() }?.constructLocale()
            PersonalDictionaryScreen(
                onClickBack = ::goBack,
                locale = locale
            )
        }
        composable(SettingsDestination.PersonalDictionaries) {
            PersonalDictionariesScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Languages) {
            LanguageScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Dictionaries) {
            DictionaryScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Layouts) {
            SecondaryLayoutScreen(onClickBack = ::goBack)
        }
        composable(SettingsDestination.Colors + "{theme}") {
            ColorsScreen(isNight = false, theme = it.arguments?.getString("theme"), onClickBack = ::goBack)
        }
        composable(SettingsDestination.ColorsNight + "{theme}") {
            ColorsScreen(isNight = true, theme = it.arguments?.getString("theme"), onClickBack = ::goBack)
        }
        composable(SettingsDestination.Subtype + "{subtype}") {
            SubtypeScreen(initialSubtype = it.arguments?.getString("subtype")!!.toSettingsSubtype(), onClickBack = ::goBack)
        }
    }
    if (target.value != SettingsDestination.Settings/* && target.value != navController.currentBackStackEntry?.destination?.route*/)
        navController.navigate(route = target.value)
}

object SettingsDestination {
    const val Settings = "settings"
    const val AppearanceHub = "appearance_hub"
    const val LayoutTypingHub = "layout_typing_hub"
    const val ProfilesHub = "profiles_hub"
    const val GesturesHub = "gestures_hub"
    const val ActionsToolbarHub = "actions_toolbar_hub"
    const val MacrosClipboardHub = "macros_clipboard_hub"
    const val LanguagesHub = "languages_hub"
    const val PrivacyDataHub = "privacy_data_hub"
    const val ExperimentalHub = "experimental_hub"
    const val AboutAdvancedHub = "about_advanced_hub"
    const val About = "about"
    const val TextCorrection = "text_correction"
    const val Preferences = "preferences"
    const val Toolbar = "toolbar"
    const val GestureTyping = "gesture_typing"
    const val DataGathering = "data_gathering" // remove when data gathering phase is done (end of 2026 latest)
    const val DataReview = "data_review" // remove when data gathering phase is done (end of 2026 latest)
    const val Advanced = "advanced"
    const val Debug = "debug"
    const val Appearance = "appearance"
    const val Kamelot = "kamelot"
    const val KamelotProfiles = "kamelot_profiles"
    const val KamelotThemes = "kamelot_themes"
    const val KamelotModules = "kamelot_modules"
    const val KamelotExperiments = "kamelot_experiments"
    const val KamelotGestureOs = "kamelot_gesture_os"
    const val KamelotQuickActions = "kamelot_quick_actions"
    const val KamelotMacros = "kamelot_macros"
    const val Colors = "colors/"
    const val ColorsNight = "colors_night/"
    const val PersonalDictionaries = "personal_dictionaries"
    const val PersonalDictionary = "personal_dictionary/"
    const val Languages = "languages"
    const val Subtype = "subtype/"
    const val Layouts = "layouts"
    const val Dictionaries = "dictionaries"
    val navTarget = MutableStateFlow(Settings)

    private val navScope = CoroutineScope(Dispatchers.Default)
    fun navigateTo(target: String) {
        if (navTarget.value == target) {
            // triggers recompose twice, but that's ok as it's a rare event
            navTarget.value = Settings
            navScope.launch { delay(10); navTarget.value = target }
        } else
            navTarget.value = target
        navScope.launch { delay(50); navTarget.value = Settings }
    }
}
