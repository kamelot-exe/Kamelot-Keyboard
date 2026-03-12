# Kamelot Keyboard Phase 1 Architecture Audit

## Current structure

### IME service entry points
- `app/src/main/AndroidManifest.xml` registers `LatinIME` as the input method service and `AndroidSpellCheckerService` as the spell checker service.
- `app/src/main/java/helium314/keyboard/latin/LatinIME.java` is the main runtime entry point. It owns the input lifecycle, `InputLogic`, `KeyboardSwitcher`, suggestion strip coordination, clipboard history manager access, and gesture consumer wiring.

### Keyboard rendering
- `app/src/main/java/helium314/keyboard/keyboard/KeyboardSwitcher.java` owns input-view creation, theme reloads, keyboard mode switching, and the bridge between `LatinIME` and keyboard view classes.
- `app/src/main/java/helium314/keyboard/keyboard/MainKeyboardView.java` and `KeyboardView.java` are the primary rendering surfaces for the main keyboard.
- `app/src/main/java/helium314/keyboard/keyboard/internal/KeyboardBuilder.kt` and `KeyboardParams.java` assemble runtime keyboard instances.

### Layout system
- `app/src/main/java/helium314/keyboard/keyboard/internal/keyboard_parser/LayoutParser.kt` parses both simple layout files and Floris-style JSON layouts.
- `app/src/main/java/helium314/keyboard/keyboard/KeyboardLayoutSet.java` selects layout variants per subtype, geometry, and mode.
- `app/src/main/java/helium314/keyboard/latin/utils/LayoutUtils.kt` and `LayoutUtilsCustom.kt` load built-in and custom layout content.
- XML and keyboard templates live under `app/src/main/res/xml` and subtype/layout resources span `app/src/main/res`.

### Gesture system
- `app/src/main/java/helium314/keyboard/latin/touchinputconsumer/GestureConsumer.java` is the runtime consumer interface for gesture input.
- `app/src/main/java/helium314/keyboard/keyboard/internal/GestureEnabler.java` and related `Gesture*` drawing/recognition classes hold gesture enablement, trail rendering, and recognition parameters.
- `app/src/main/java/helium314/keyboard/settings/screens/GestureTypingScreen.kt` exposes gesture user settings.
- Gesture data gathering is a separate temporary flow under `settings/screens/gesturedata`.

### Theme system
- `app/src/main/java/helium314/keyboard/keyboard/KeyboardTheme.kt` is the main theme/style/color resolver.
- `app/src/main/java/helium314/keyboard/settings/screens/AppearanceScreen.kt`, `ColorsScreen.kt`, and `ColorThemePickerDialog.kt` expose theme and color customization.
- Theme resources are still tightly coupled to the current keyboard engine and resource IDs.

### Settings/preferences
- `app/src/main/java/helium314/keyboard/settings/SettingsNavHost.kt` defines the Compose settings routes.
- `app/src/main/java/helium314/keyboard/settings/SettingsContainer.kt` centralizes searchable settings registration.
- `app/src/main/java/helium314/keyboard/latin/settings/Settings.java` contains the long-lived preference key namespace and settings reload logic.
- `app/src/main/java/helium314/keyboard/latin/settings/Defaults.kt` stores default values.

### Clipboard, suggestions, languages
- Clipboard: `ClipboardHistoryManager.kt`, `ClipboardHistoryEntry.kt`, `keyboard/clipboard/*`, `latin/database/ClipboardDao.kt`.
- Suggestions: `Suggest.kt`, `SuggestedWords.java`, `latin/suggestions/*`, and JNI suggest code under `app/src/main/jni/src/suggest`.
- Language/subtype management: `SubtypeSettings.kt`, `SubtypeUtils.kt`, `RichInputMethodSubtype.kt`, and `KeyboardLayoutSet.java`.

## What should stay as-is in Phase 1
- `LatinIME` remains the only IME service entry point.
- `KeyboardSwitcher`, `KeyboardLayoutSet`, `LayoutParser`, and `KeyboardTheme` remain the primary runtime integration points.
- Existing preference storage in shared preferences remains the source of truth.
- JNI-backed suggestion and dictionary paths remain untouched.
- Working keyboard rendering, subtype switching, clipboard history, and gesture typing remain behaviorally unchanged.

## Best extension points for Kamelot
- Additive configuration layer:
  - `app/src/main/java/helium314/keyboard/kamelot/*`
  - Safe place for product constants, flags, preset definitions, and future model types.
- Settings surfacing:
  - Add new routes and `Setting` registrations through `SettingsNavHost.kt`, `MainSettingsScreen.kt`, and `SettingsContainer.kt`.
- Theme preset groundwork:
  - Keep current rendering in `KeyboardTheme.kt`, but define future preset metadata separately and map it later.
- Profile groundwork:
  - Keep profile models outside `SettingsValues` for now. Introduce data definitions first, then bind them in later phases once switching rules are clear.
- Layout experiments:
  - Keep experimental layout modes separate from `LayoutParser` runtime decisions until there is a tested fallback path.
- Gesture experiments:
  - Gate all future gesture changes behind explicit Kamelot flags, keeping `GestureConsumer` and `GestureEnabler` untouched until dedicated testing exists.

## Phase 1 conclusion
- The codebase already has clear seams for settings, themes, layouts, and runtime switching.
- The highest-risk area is the live IME path rooted in `LatinIME` and `KeyboardSwitcher`.
- The safest Phase 1 strategy is additive metadata, flags, and documentation on top of the current HeliBoard-derived runtime.
