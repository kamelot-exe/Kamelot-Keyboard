# Kamelot Quick Actions And Macros Notes

## Overview

Phase 4 adds a profile-aware strip foundation on top of the existing suggestion strip without replacing the current HeliBoard toolbar or suggestion pipeline.

The active Kamelot profile now owns:

- a `QuickActionsConfig`
- ordered quick action definitions
- ordered macro strip definitions
- context hint metadata for future context-aware profiles

All strip items resolve to `KeyboardAction` instances and dispatch through the existing Kamelot action engine.

## Resolution Flow

1. `KamelotProfileManager` loads the active profile.
2. `KamelotQuickActionsResolver` resolves the profile config into visible `KamelotStripItem` instances.
3. `LatinIME.updateSuggestionStripView()` binds those items into `SuggestionStripView`.
4. `SuggestionStripView` dispatches strip taps through `KamelotActionHost`.
5. `KamelotActionDispatcher` resolves macros and forwards execution to `KamelotActionExecutor`.

There is no parallel execution path for strip actions.

## Keyboard Builder Direction

Phase 4 stores the first builder-oriented composition metadata:

- `stripMode`
- `stripPreset`
- per-item `enabled`
- per-item `order`
- per-profile strip membership

This keeps the runtime additive while making future builder work largely a settings/editor problem instead of a runtime architecture rewrite.

## Context Profiles Direction

Profiles now carry `contextHints` such as:

- `CHAT`
- `WORK`
- `BROWSING`
- `NOTES`
- `CODING`

These hints are documentation and storage groundwork only in Phase 4. No automatic app detection or profile switching is performed yet.

## Future Reuse

Future gesture routing should call into the same dispatcher used by the strip.

Future toolbar, macro buttons, launcher actions, and profile shortcuts should all keep using:

- `KamelotActionDispatcher`
- `KamelotActionExecutor`
- `KamelotActionContext`

That keeps action behavior consistent across all Kamelot entry points.
