// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.JniUtils
import helium314.keyboard.latin.utils.NextScreenIcon
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.settings.initPreview

private data class SettingsHubCard(
    val title: String,
    val summary: String,
    val labels: List<String> = emptyList(),
    val onClick: () -> Unit,
)

@Composable
fun MainSettingsScreen(
    onClickAppearance: () -> Unit,
    onClickLayoutTyping: () -> Unit,
    onClickProfiles: () -> Unit,
    onClickGestures: () -> Unit,
    onClickActionsToolbar: () -> Unit,
    onClickMacrosClipboard: () -> Unit,
    onClickLanguages: () -> Unit,
    onClickPrivacyData: () -> Unit,
    onClickExperimental: () -> Unit,
    onClickAboutAdvanced: () -> Unit,
    onClickBack: () -> Unit,
) {
    val cards = listOf(
        SettingsHubCard(
            title = stringResource(R.string.settings_section_appearance_title),
            summary = stringResource(R.string.settings_section_appearance_summary),
            labels = listOf(
                stringResource(R.string.kamelot_themes_module_title),
                stringResource(R.string.theme_style),
            ),
            onClick = onClickAppearance,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_layout_typing_title),
            summary = stringResource(R.string.settings_section_layout_typing_summary),
            labels = listOf(
                stringResource(R.string.settings_screen_preferences),
                stringResource(R.string.settings_screen_correction),
                stringResource(R.string.settings_screen_secondary_layouts),
            ),
            onClick = onClickLayoutTyping,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_profiles_title),
            summary = stringResource(R.string.settings_section_profiles_summary),
            labels = listOf(
                stringResource(R.string.kamelot_profiles_module_title),
                stringResource(R.string.kamelot_theme_preset_selector_title),
            ),
            onClick = onClickProfiles,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_gestures_title),
            summary = stringResource(R.string.settings_section_gestures_summary),
            labels = listOf(
                stringResource(R.string.settings_screen_gesture),
                stringResource(R.string.kamelot_gesture_os_title),
            ),
            onClick = onClickGestures,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_actions_toolbar_title),
            summary = stringResource(R.string.settings_section_actions_toolbar_summary),
            labels = listOf(
                stringResource(R.string.settings_screen_toolbar),
                stringResource(R.string.kamelot_quick_actions_title),
            ),
            onClick = onClickActionsToolbar,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_macros_clipboard_title),
            summary = stringResource(R.string.settings_section_macros_clipboard_summary),
            labels = listOf(
                stringResource(R.string.kamelot_macros_title),
                stringResource(R.string.clipboard),
            ),
            onClick = onClickMacrosClipboard,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_languages_title),
            summary = stringResource(R.string.settings_section_languages_summary),
            labels = listOf(
                stringResource(R.string.language_and_layouts_title),
                stringResource(R.string.dictionary_settings_category),
            ),
            onClick = onClickLanguages,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_privacy_title),
            summary = stringResource(R.string.settings_section_privacy_summary),
            labels = listOf(
                stringResource(R.string.incognito),
                stringResource(R.string.enable_clipboard_history),
            ),
            onClick = onClickPrivacyData,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_experimental_title),
            summary = stringResource(R.string.settings_section_experimental_summary),
            labels = listOf(
                stringResource(R.string.kamelot_gesture_os_title),
                stringResource(R.string.kamelot_hex_module_title),
            ),
            onClick = onClickExperimental,
        ),
        SettingsHubCard(
            title = stringResource(R.string.settings_section_about_advanced_title),
            summary = stringResource(R.string.settings_section_about_advanced_summary),
            labels = listOf(
                stringResource(R.string.settings_screen_advanced),
                stringResource(R.string.settings_screen_about),
            ),
            onClick = onClickAboutAdvanced,
        ),
    )

    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.ime_settings),
        settings = emptyList(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SettingsHeroCard(
                title = stringResource(R.string.settings_hub_title),
                subtitle = stringResource(R.string.settings_hub_summary),
            )
            cards.forEach { card ->
                SettingsHubCardView(card)
            }
            if (JniUtils.sHaveGestureLib) {
                SettingsMiniCard(
                    title = stringResource(R.string.settings_screen_gesture),
                    summary = stringResource(R.string.settings_hub_gesture_summary),
                    onClick = onClickGestures,
                )
            }
            SettingsSearchHint()
        }
    }
}

@Composable
private fun SettingsHeroCard(title: String, subtitle: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SettingsHubCardView(card: SettingsHubCard) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = card.onClick),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = card.title, style = MaterialTheme.typography.titleLarge)
                NextScreenIcon()
            }
            Text(
                text = card.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (card.labels.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    card.labels.forEach { label ->
                        AssistChip(
                            onClick = {},
                            enabled = false,
                            label = { Text(label) },
                            colors = AssistChipDefaults.assistChipColors(
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            border = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsMiniCard(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SettingsSearchHint() {
    Surface(shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(text = stringResource(R.string.kamelot_global_search_title), style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(R.string.settings_hub_search_summary),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    initPreview(LocalContext.current)
    Theme(previewDark) {
        Surface {
            MainSettingsScreen({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {})
        }
    }
}
