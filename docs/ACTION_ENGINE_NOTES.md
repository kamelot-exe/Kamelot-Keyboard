# Action Engine Notes

## Resolution flow
1. A caller creates a `KamelotActionContext`.
2. The caller passes a stored `KeyboardAction` to `KamelotActionDispatcher`.
3. The dispatcher resolves macro-backed actions first.
4. The resolved action is executed by `KamelotActionExecutor`.
5. Execution returns a `KamelotActionResult` instead of throwing or assuming support.

## Context design
- `KamelotActionContext` intentionally carries only small, explicit dependencies:
  - `InputConnection`
  - `EditorInfo`
  - selected-text provider
  - `KamelotProfileManager`
  - macro map
  - optional clipboard hook
  - dry-run flag
- This avoids binding the action engine directly to large singleton runtime state.

## Future gesture integration
- Gesture code should not execute actions directly.
- Gesture routing should build a `KamelotActionContext` from the current IME session and then call `KamelotActionDispatcher`.
- Unsupported actions should continue returning structured results so gesture code can ignore or log them safely.

## Future toolbar and macro integration
- Toolbar buttons should store or reference `KeyboardAction` definitions.
- Macro buttons should resolve through the dispatcher rather than inserting text through a parallel code path.
- Profile-specific quick actions should read from stored profile actions and macros, then dispatch through the same engine.

## Future launcher integration
- Launcher actions should also go through the dispatcher.
- Add launcher-specific hooks to `KamelotActionContext` only when that feature is intentionally introduced.
- Keep external-app behavior feature-gated and separate from the current supported action subset.
