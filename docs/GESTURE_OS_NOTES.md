# Gesture OS Notes

## Phase 6 Scope

Phase 6 introduces the first Gesture OS layer without touching swipe typing or JNI gesture recognition.

Current supported triggers are intentionally narrow:

- space swipe left
- space swipe right
- space swipe up

These triggers are routed through the Kamelot action engine and remain feature-gated.

## What Was Preserved

Existing gesture typing remains intact because Phase 6 does not modify:

- JNI suggestion code
- gesture recognition internals
- batch word swipe logic

Gesture OS is layered on top of the already existing spacebar swipe hook in `KeyboardActionListenerImpl`.

If no Kamelot Gesture OS binding is active, the original HeliBoard-derived space swipe behavior still runs.

## Current Action Routing

When a supported trigger fires:

1. `KeyboardActionListenerImpl` checks the Gesture OS feature flag.
2. The active profile is loaded.
3. `KamelotGestureOsResolver` resolves a binding for the trigger.
4. The binding action is sent through `KamelotActionDispatcher`.
5. Execution happens in `KamelotActionExecutor`.

There is no gesture-specific action execution path outside the dispatcher.

## Current Safe Bindings

Phase 6 only uses safe actions already supported by the executor:

- `MOVE_CURSOR_LEFT`
- `MOVE_CURSOR_RIGHT`
- `OPEN_CLIPBOARD`

Clipboard opening only uses the existing clipboard keyboard path.

## Profile Awareness

Gesture bindings are stored per profile.

Current seeded behavior:

- `Chat`: cursor left/right plus clipboard on swipe up
- `Work`: cursor-focused left/right
- `Minimal`: Gesture OS off
- `OneHand`: simplified cursor gestures

This makes Gesture OS compatible with Context Profiles from the start.

## Future Layering

Future Gesture OS work should continue in layers:

1. Keep gesture typing untouched.
2. Add higher-level directional bindings only where the trigger source is already stable.
3. Reuse the same dispatcher for hold-and-swipe and key-direction gestures.
4. Let Keyboard Builder edit gesture bindings using the same profile model.

That keeps Gesture OS, Context Profiles, and Keyboard Builder aligned around one action architecture.
