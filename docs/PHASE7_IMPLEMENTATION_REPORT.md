# Phase 7 Implementation Report

## Files Changed

- `app/src/main/java/helium314/keyboard/kamelot/KamelotConfig.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotLayoutModeResolver.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotProfileManager.kt`
- `app/src/main/java/helium314/keyboard/kamelot/layout/KamelotLayoutMetadata.kt`
- `app/src/main/java/helium314/keyboard/kamelot/layout/HexKeyGeometry.kt`
- `app/src/main/java/helium314/keyboard/kamelot/layout/HexGridLayout.kt`
- `app/src/main/java/helium314/keyboard/kamelot/layout/HexTouchMapper.kt`
- `app/src/main/java/helium314/keyboard/keyboard/Keyboard.java`
- `app/src/main/java/helium314/keyboard/keyboard/KeyDetector.java`
- `app/src/main/java/helium314/keyboard/keyboard/MainKeyboardView.java`
- `app/src/main/java/helium314/keyboard/keyboard/KeyboardView.java`
- `app/src/main/java/helium314/keyboard/keyboard/internal/KeyboardParams.java`
- `app/src/main/java/helium314/keyboard/keyboard/internal/keyboard_parser/LayoutParser.kt`
- `app/src/main/java/helium314/keyboard/keyboard/internal/keyboard_parser/KeyboardParser.kt`
- `app/src/main/java/helium314/keyboard/settings/screens/KamelotScreen.kt`
- `app/src/main/res/values/strings.xml`
- `docs/HEX_LAYOUT_ARCHITECTURE.md`

## Systems Introduced

- Experimental profile-selectable `HEX_EXPERIMENTAL` layout mode
- Hex geometry caching
- Hex polygon touch mapping
- Hex background rendering path
- JSON layout metadata parsing for future authored hex layouts
- Adaptive hex hit zones
- Predictive hex swipe path preprocessing

## Rendering and Runtime Impact

- Standard layout mode stays unchanged.
- Hex mode activates only when the active profile requests it and the hex feature flag is enabled.
- Existing `Key`, `Keyboard`, `KeyboardView`, and `KeyDetector` logic remains the default fallback path.
- No LatinIME lifecycle, JNI suggestion, or swipe typing rewrite was introduced.
- Adaptive hit zones are updated only after successful key sends in hex mode.
- Predictive swipe preprocessing runs only for hex mode when its dedicated experimental flag is enabled.

## Extension Points

- Add dedicated hex-authored layouts through the new JSON wrapper metadata
- Promote per-profile hex spacing and row offset controls into Keyboard Builder
- Add hex-aware key previews later
- Keep future gesture routing on top of the shared action system and touch-mapping output
- Extend adaptive hit modeling with decay or per-profile storage only after enough real-world testing
- Expand predictive swipe cleanup beyond nearest-center snapping only if the current lightweight resolver proves stable

## Intentionally Deferred

- Dedicated hex layout assets
- Hex-specific popup preview placement
- JNI proximity changes
- Automatic context-based hex profile routing
- Gesture-over-key directional actions in hex mode
- Any dual-layer, top/bottom split, or symbol-inside-hex behavior

## Verification Notes

- Changes were implemented to remain additive and compile-safe.
- Full Gradle verification is still blocked in this environment by missing Android SDK configuration.
