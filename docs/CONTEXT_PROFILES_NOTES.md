# Context Profiles Notes

## What Phase 5 Adds

Kamelot profiles now shape visible keyboard-adjacent controls, not just stored theme or macro data.

Each profile can now influence:

- quick panel preset
- strip behavior
- profile switcher visibility
- macro strip availability
- quick panel item limit
- context hints
- optional app-category routing intent

## Current Safe Model

The active profile carries two context layers:

1. `contextHints`
2. `routingIntent`

`contextHints` describe how the profile should be thought about inside Kamelot:

- chat
- work
- browsing
- notes
- coding

`routingIntent` is the first safe hook for future app-aware routing. It currently stores:

- target app category
- `manualOnly`

Phase 5 keeps `manualOnly = true` for all seeded profiles. That means no automatic app detection is performed yet.

## Future App-Aware Routing

Safe future expansion can happen in this order:

1. Detect app package names only locally on-device.
2. Map package names to coarse app categories.
3. Resolve category to a preferred Kamelot profile.
4. Prompt or suggest before automatic switching.
5. Keep a strong per-profile opt-out.

This avoids pushing profile switching into the IME typing path too early.

## Keyboard Builder Connection

Context Profiles and Keyboard Builder are now connected through the same profile config object.

Builder-facing configuration now includes:

- quick panel preset
- item ordering
- per-item enabled state
- macro strip enablement
- strip visibility behavior
- profile switch affordances

That means future builder work can expand profile composition without replacing the storage model again.
