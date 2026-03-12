# Kamelot Keyboard Phase 3 Implementation Report

## Files changed
- `app/src/main/java/helium314/keyboard/kamelot/KamelotDefaults.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotMacro.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotProfile.kt`
- `app/src/main/java/helium314/keyboard/kamelot/KamelotProfileManager.kt`
- `app/src/main/java/helium314/keyboard/kamelot/actions/KamelotActionContext.kt`
- `app/src/main/java/helium314/keyboard/kamelot/actions/KamelotActionDispatcher.kt`
- `app/src/main/java/helium314/keyboard/kamelot/actions/KamelotActionExecutor.kt`
- `app/src/main/java/helium314/keyboard/kamelot/actions/KamelotActionResult.kt`
- `app/src/main/java/helium314/keyboard/settings/screens/KamelotScreen.kt`
- `app/src/main/res/values/strings.xml`
- `docs/ACTION_ENGINE_NOTES.md`
- `docs/PHASE3_IMPLEMENTATION_REPORT.md`

## Action types supported
- `INSERT_TEXT`
- `MOVE_CURSOR_LEFT`
- `MOVE_CURSOR_RIGHT`
- `SWITCH_PROFILE`
- `OPEN_CLIPBOARD`
  - only through an explicit runtime hook in `KamelotActionContext`
  - currently dry-run capable, otherwise safely unsupported when no hook is provided
- `RUN_MACRO`
  - supports macro id resolution to text payload
  - falls back to direct text payload if provided

## Unsupported or intentionally deferred
- `DELETE_WORD`
- launcher actions
- external app launching
- gesture-engine wiring
- toolbar UI execution wiring
- macro editor UI
- clipboard opening from the live IME path

## Runtime integration points added
- New reusable action engine package under `helium314.keyboard.kamelot.actions`.
- `KamelotProfile` now includes a macro list for future quick-insert features.
- `KamelotProfileManager` now exposes a macro map for dispatcher resolution.
- Kamelot settings now include a dev-only dry-run diagnostics row that resolves and exercises active-profile actions without mutating IME runtime state.

## Risks
- Cursor movement uses conservative `InputConnection` APIs only and may be ignored by some editors, but it should fail safely.
- `OPEN_CLIPBOARD` is intentionally not wired to the live keyboard runtime yet.
- No live action entry point was added to `LatinIME`, `KeyboardSwitcher`, or rendering code in this phase.

## Next extension points
- Build a dedicated runtime action context from the IME layer when toolbar or gesture integration starts.
- Route toolbar buttons through `KamelotActionDispatcher` instead of bespoke handlers.
- Route future gesture or launcher actions through the same executor with feature-flagged hooks.
- Add macro editing and profile action customization on top of the stored models already added here.
