# Next Phase Recommendations

## Recommended build order
1. Introduce persistent Kamelot profile storage and profile selection rules.
2. Add theme preset mapping on top of the existing `KeyboardTheme` engine.
3. Extract action definitions for swipe, toolbar, and future launcher actions.
4. Add internal layout metadata needed for experimental modes.
5. Expand gesture configuration only after action routing and profile scoping exist.

## Modify first
- `app/src/main/java/helium314/keyboard/kamelot/*`
- `app/src/main/java/helium314/keyboard/settings/*`
- `app/src/main/java/helium314/keyboard/keyboard/KeyboardTheme.kt`
- `app/src/main/java/helium314/keyboard/keyboard/internal/keyboard_parser/*`
- `app/src/main/java/helium314/keyboard/keyboard/KeyboardSwitcher.java`

## Risk areas
- `LatinIME.java`: core lifecycle and editor interaction.
- `KeyboardSwitcher.java`: theme reloads, keyboard replacement, and view state transitions.
- `SettingsValues.java`: broad fan-out into runtime behavior.
- JNI suggestion and dictionary code: high blast radius and harder validation loop.

## What to test first
- IME enablement and launcher/settings entry.
- Keyboard rendering after settings changes.
- Subtype switching and layout fallback behavior.
- Suggestion strip visibility and clipboard history behavior.
- Gesture typing still working with all new Kamelot flags disabled.

## Hex layout approach
- Start with metadata and parser capability checks only.
- Keep hex mode behind a hard-disabled feature flag until there is a fallback-to-standard path for every subtype.
- Avoid reusing rectangular hit-testing assumptions without explicit abstraction.
- Prototype hex geometry outside the main rendering path first.

## Gesture engine approach
- Introduce a configuration abstraction before changing runtime gesture classes.
- Gate any advanced gesture routing with Kamelot flags and leave current gesture recognition untouched.
- Validate trail rendering, floating preview, and editor commit flow separately.
- Preserve current defaults even when the advanced gesture experiment flag exists.

## Play Store and privacy readiness
- Defer package/application ID migration to a dedicated release plan.
- Audit permissions and explain every retained permission in store-facing docs.
- Replace inherited project links and metadata with Kamelot-owned assets before store submission.
- Add explicit backup/export documentation and privacy copy for clipboard, personalization, and gesture data features.
- Keep analytics absent and network usage opt-in or nonexistent.
