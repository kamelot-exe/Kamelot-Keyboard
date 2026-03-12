# Kamelot Keyboard Architecture Plan

## Goal
Create a modular direction without forcing a module split before the code is ready. Phase 1 keeps the current app intact and introduces naming, models, and extension seams for later extraction.

## Target structure

### `ime-core`
- Own IME lifecycle, editor interaction, input sessions, and service wiring.
- Starts as a conceptual boundary around `LatinIME`, input attributes, and IME state coordination.

### `keyboard-engine`
- Own keyboard switching, keyboard state transitions, rendering orchestration, and view composition.
- Starts around `KeyboardSwitcher`, `KeyboardState`, `MainKeyboardView`, and `KeyboardBuilder`.

### `layout-engine`
- Own layout parsing, layout metadata, geometry-aware selection, and future non-rectangular layouts.
- Starts around `LayoutParser`, `KeyboardLayoutSet`, subtype layout resolution, and custom layout loading.

### `gesture-engine`
- Own gesture enablement, trail rendering, recognition parameters, and future gesture action routing.
- Starts around `GestureConsumer`, `GestureEnabler`, and the gesture drawing/recognition internals.

### `theme-engine`
- Own styles, color tokens, preset definitions, and background/font customization.
- Starts around `KeyboardTheme`, appearance settings, and color customization dialogs.

### `action-engine`
- Own key actions, swipe actions, toolbar actions, and future action composition.
- Starts around `KeyboardActionListener`, toolbar key handling, and settings-backed action preferences.

### `profiles`
- Own saved keyboard profiles, per-profile settings snapshots, and context-aware profile selection.
- Phase 1 only introduces preset data models and flags.

### `clipboard`
- Own clipboard history storage, display, retention rules, and future dock enhancements.
- Existing clipboard classes are already fairly isolated and can evolve in place before extraction.

### `macros`
- Future home for user-authored text/action shortcuts and macro strip UI.
- No runtime behavior should be added until action routing and storage formats are defined.

### `launcher`
- Future entry point for launchable shortcuts or actions outside the IME surface.
- Should remain disabled until Android app-surface behavior and privacy implications are defined.

### `settings`
- Own UI composition for settings, search, routing, and preference surfacing.
- Current Compose settings structure is already a good host for incremental growth.

## Practical rollout order
1. Stabilize `kamelot` config models and preference keys.
2. Add profile storage models without changing runtime switching.
3. Add theme preset mapping without changing `KeyboardTheme` defaults.
4. Introduce an action registry abstraction above current swipe and toolbar actions.
5. Extract gesture configuration and runtime gating.
6. Extend layout metadata for experimental modes with guaranteed fallback to standard layouts.
7. Split implementation packages only after the boundaries are enforced in code.

## Non-goals for now
- No package rename.
- No application ID rename.
- No immediate Gradle multi-module refactor.
- No runtime hex layout.
- No gesture engine rewrite.

## Design rules
- Keep current IME behavior as the default path.
- New features must ship behind explicit flags.
- Every experimental path needs a standard-layout fallback.
- Privacy-sensitive features must default to offline and opt-in.
