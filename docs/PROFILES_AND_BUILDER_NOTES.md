# Kamelot Profiles And Builder Notes

## Profiles As A Signature Feature

Profiles are now treated as a first-class Kamelot product surface rather than a hidden internal model.

The current user-facing profile system supports:

- visible profile list
- active profile highlight
- manual switching
- duplication of the active profile
- renaming custom profiles
- deletion of custom profiles

Default presets remain protected:

- Chat
- Work
- Minimal
- OneHand

## What A Profile Carries

Each profile now captures a broader keyboard recipe:

- theme preset
- layout mode
- appearance config
- quick panel config
- macros
- gesture bindings
- context hints
- routing intent

This keeps profiles aligned with the long-term product formula: the keyboard should adapt to the user instead of forcing one universal setup.

## Builder Direction

Kamelot is not yet a drag-and-drop builder, but the configuration surface is now builder-shaped.

Users can already influence:

- active theme preset
- active layout mode
- quick panel preset
- visible quick actions
- macro strip behavior
- gesture preset
- spacing and appearance recipe metadata

The settings structure is designed so future builder work can extend these same primitives instead of creating a second parallel customization system.

## Context Profiles

Automatic app-aware routing is still deferred, but the model is ready for it.

Today:

- profiles can be switched manually
- profiles store context hints
- profiles store routing intent metadata

Later safe expansion:

- app-category mapping
- per-app suggestions for profile switching
- optional auto-switching with explicit user control

## Safety Rules

The profile system intentionally protects runtime stability:

- default profiles cannot be deleted
- invalid stored profile IDs fall back safely
- profile switching continues to route through the existing theme and action hooks
- profile duplication clones stable state instead of constructing incomplete profiles from scratch

## Recommended Next Expansions

1. Add a richer custom profile editor with theme/layout/quick-panel controls in one modal flow.
2. Add profile-scoped clipboard behavior settings.
3. Add profile import/export once the builder data model stabilizes.
4. Add app-aware routing only after explicit UX for overrides and fallback behavior is ready.
