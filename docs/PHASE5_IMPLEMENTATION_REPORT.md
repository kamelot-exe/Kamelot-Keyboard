# Phase 5 Implementation Report

## Summary

Phase 5 expands the Kamelot quick panel into a profile-aware keyboard-adjacent control layer.

The quick panel now:

- reads richer profile config
- exposes active profile switching directly near the keyboard
- varies visible controls by profile preset and strip behavior
- moves further toward a Keyboard Builder model without a large runtime rewrite

## Files Changed

### Profile and builder model

- `app/src/main/java/helium314/keyboard/kamelot/KamelotQuickActions.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotProfile.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotDefaults.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotProfileManager.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotQuickActionsResolver.kt`

### Action integration

- `app/src/main/java/helium314/keyboard/kamelot/actions/KamelotActionDispatcher.kt`

### Runtime quick panel

- `app/src/main/java/helium314/keyboard/latin/LatinIME.java`
- `app/src/main/java/helium314/keyboard/latin/suggestions/SuggestionStripView.kt`

### Settings and configuration UI

- `app/src/main/java/helium314/keyboard/settings/screens/KamelotQuickActionsScreen.kt`
- `app/src/main/java/helium314/keyboard/settings/screens/KamelotScreen.kt`
- `app/src/main/res/values/strings.xml`

### Documentation

- `docs/PHASE5_IMPLEMENTATION_REPORT.md`
- `docs/CONTEXT_PROFILES_NOTES.md`

## What Was Implemented

### Active-profile-aware quick panel

The quick panel now resolves from:

- active profile config
- available stored profiles
- quick panel preset
- strip behavior
- macro strip enablement
- item limit

Panel composition is no longer just a flat list of actions.

### Manual profile switching from the keyboard

The quick panel can now render:

- a bold active profile badge
- explicit switches to other profiles

These buttons dispatch through the existing Kamelot action engine using `SWITCH_PROFILE`.

### Stronger Context Profiles

Profiles now store:

- `routingIntent`
- `quickPanelPreset`
- `stripBehavior`
- `macroStripEnabled`
- `showProfileSwitcher`
- `showProfileBadge`
- `maxVisibleItems`

This is still manual-first and safe. No automatic app routing was introduced.

### Keyboard Builder groundwork

The settings UI now supports lightweight builder-style configuration for the active profile:

- choose quick panel preset
- choose strip behavior
- enable or disable macro strip
- show or hide profile badge
- show or hide profile switch buttons
- cap visible item count
- move quick actions up or down
- move macro strip items up or down
- set a future routing category intent

## Runtime Impact

The quick panel remains feature-gated through the existing Kamelot macro strip flag.

When enabled, different profiles now produce visibly different keyboard-adjacent controls:

- `Chat` favors macros and communication-oriented panel behavior
- `Work` favors cursor controls and productivity-oriented panel behavior
- `Minimal` keeps the panel sparse
- `OneHand` emphasizes profile switching and compact controls

## Deferred

Still deferred intentionally:

- automatic app-based profile switching
- drag-and-drop builder UI
- launcher or external app actions
- gesture-triggered profile/action routing
- hex layout
- gesture engine rewrite

## Verification

Available verification remains partial.

`.\gradlew.bat :app:compileDebugKotlin` still fails before compilation because the environment has no configured Android SDK path:

- missing `ANDROID_HOME`
- missing `local.properties` with `sdk.dir`

So full compile verification was not possible here.
