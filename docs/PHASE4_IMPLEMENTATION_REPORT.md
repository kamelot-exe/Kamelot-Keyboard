# Phase 4 Implementation Report

## Summary

Phase 4 adds a safe Macro Strip + Quick Actions foundation for Kamelot Keyboard.

The implementation stays additive:

- existing IME typing behavior remains intact by default
- the new strip is feature-gated
- action taps reuse the Phase 3 action engine
- profile storage now carries strip composition data
- macro editing is available from Kamelot settings

## Files Changed

### Models and profile storage

- `app/src/main/java/helium314/keyboard/kamelot/KamelotProfile.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotDefaults.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotProfileManager.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotQuickActions.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotQuickActionsResolver.kt`

### Action engine wiring

- `app/src/main/java/helium314/keyboard/kamelot/actions/KamelotActionContext.kt`
- `app/src/main/java/helium314/keyboard/kamelot/actions/KamelotActionExecutor.kt`
- `app/src/main/java/helium314/keyboard/kamelot/actions/KamelotActionDispatcher.kt`

### Runtime strip integration

- `app/src/main/java/helium314/keyboard/latin/LatinIME.java`
- `app/src/main/java/helium314/keyboard/latin/suggestions/SuggestionStripView.kt`
- `app/src/main/res/layout/suggestions_strip.xml`

### Settings and editing UI

- `app/src/main/java/helium314/keyboard/settings/SettingsNavHost.kt`
- `app/src/main/java/helium314/keyboard/settings/screens/KamelotScreen.kt`
- `app/src/main/java/helium314/keyboard/settings/screens/KamelotQuickActionsScreen.kt`
- `app/src/main/java/helium314/keyboard/settings/screens/KamelotMacrosScreen.kt`
- `app/src/main/java/helium314/keyboard/settings/dialogs/KamelotMacroEditorDialog.kt`
- `app/src/main/res/values/strings.xml`

### Documentation

- `docs/QUICK_ACTIONS_AND_MACROS_NOTES.md`
- `docs/PHASE4_IMPLEMENTATION_REPORT.md`

## Systems Introduced

### Quick actions and macro strip model

Added:

- `QuickActionItem`
- `MacroStripItem`
- `QuickActionsConfig`
- strip mode and strip preset enums
- context profile hint enums

### Profile-scoped strip composition

Each seeded profile now carries:

- default quick actions
- default visible macros
- strip mode metadata
- strip preset metadata
- context hints

### Runtime strip foundation

`SuggestionStripView` now has an additive Kamelot quick-actions container.

This container:

- is separate from the legacy toolbar keys
- is bound from the active Kamelot profile
- stays hidden unless the macro strip feature flag is enabled
- dispatches taps through the Kamelot action engine

### Macro editing MVP

Kamelot settings now support:

- listing active-profile macros
- adding a macro
- editing label and text payload
- deleting a macro

New macros are added to the active profile and also seeded into that profile's macro strip configuration.

## UI Integration Points

The runtime UI hook is intentionally small:

- `LatinIME.updateSuggestionStripView()` binds the active profile strip items and action host
- `SuggestionStripView` renders strip buttons and forwards taps to the dispatcher

No heavy rewrites were made to:

- `LatinIME` typing logic
- `KeyboardSwitcher`
- layout parsing
- gesture recognition

## Dispatch Behavior

Strip items dispatch through:

1. `SuggestionStripView.KamelotActionHost`
2. `KamelotActionDispatcher`
3. `KamelotActionExecutor`

Supported strip behavior in this phase:

- text macro insertion
- cursor left/right
- profile switching
- clipboard opening through the existing clipboard keyboard path

## Deferred Work

Still intentionally deferred:

- full drag-and-drop Keyboard Builder UI
- arbitrary user-defined action authoring
- launcher and external app actions
- automatic context/app detection
- gesture-triggered strip/action routing
- hex layout work
- large visual redesign of the strip

## Runtime Impact

Default behavior stays HeliBoard-like unless Kamelot features are explicitly enabled.

The new strip:

- is off by default behind `kamelot_enable_macro_strip`
- also depends on the existing experimental features master switch
- reuses current theme and strip sizing instead of introducing a new visual system

## Verification Notes

Attempted verification:

- `.\gradlew.bat :app:compileDebugKotlin`

The build still stops before compilation because the environment does not have an Android SDK path configured:

- missing `ANDROID_HOME`
- or missing `local.properties` with `sdk.dir`

No full compile verification was possible in this environment.
