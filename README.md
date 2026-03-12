# Kamelot Keyboard

Kamelot Keyboard is a privacy-first Android keyboard forked from the HeliBoard/OpenBoard lineage and being reshaped into a more adaptive keyboard system.

Project direction:

- private and offline-first
- deeply customizable
- gesture-first
- profile-aware
- modular by design
- Play Store-ready in direction, without adding analytics or network dependence

Core product line:

> A private, customizable Android keyboard that adapts to the user instead of forcing the user to adapt to it.

## Current State

This repository is no longer a plain HeliBoard fork. It now contains additive Kamelot foundations built in phases on top of the existing IME runtime.

Already in place:

- Kamelot foundation/config layer
- stored keyboard profiles
- theme preset abstraction
- action engine and dispatcher
- macro and quick-actions groundwork
- profile-aware quick panel foundation
- Gesture OS foundation
- experimental hex layout foundation
- optional adaptive hex hit zones
- optional predictive hex swipe path preprocessing

Important:

- standard rectangular layouts remain the default
- core LatinIME behavior is intentionally preserved as the fallback path
- experimental systems are feature-gated

## What Kamelot Is Building Toward

Kamelot is moving toward three long-term product systems:

1. Context Profiles
2. Keyboard Builder
3. Gesture OS

Experimental hex layouts are also being prepared as an optional advanced layout mode, not as a replacement for the current keyboard.

## Privacy

Kamelot Keyboard keeps the privacy-first direction of its lineage:

- no analytics
- no network-dependent product features
- local-only experimental adaptation data
- offline-first architecture decisions

Some inherited optional gesture-typing support from the upstream ecosystem may still depend on external proprietary libraries if a developer chooses to wire them in manually, but Kamelot itself does not introduce network features or telemetry.

## Repository Structure

Main app code lives under:

- `app/src/main/java/helium314/keyboard`

Kamelot-specific additions are primarily under:

- `app/src/main/java/helium314/keyboard/kamelot`
- `docs`

Key documentation:

- `docs/PHASE1_ARCHITECTURE_AUDIT.md`
- `docs/KAMELOT_ARCHITECTURE_PLAN.md`
- `docs/NEXT_PHASE_RECOMMENDATIONS.md`
- `docs/PHASE7_IMPLEMENTATION_REPORT.md`
- `docs/HEX_LAYOUT_ARCHITECTURE.md`
- `docs/HEX_USABILITY_ENHANCEMENTS.md`

## Building

This is an Android project built with Gradle.

Typical local build command:

```powershell
.\gradlew.bat :app:assembleDebug
```

You need a working Android SDK configured through `ANDROID_HOME` or `local.properties`.

## Scope and Non-Goals

Current Kamelot work is intentionally additive.

Not being done in the current direction:

- no package rename at risky stages
- no massive rewrite of LatinIME
- no full gesture engine rewrite
- no JNI suggestion engine rewrite
- no analytics
- no cloud sync
- no fake AI features

## Origin and Licensing

Kamelot Keyboard is built on top of the HeliBoard/OpenBoard/AOSP keyboard lineage.

That heritage matters for both engineering and licensing:

- this repository includes GPL-licensed fork work
- Apache-licensed AOSP lineage files are also present where applicable

See:

- `LICENSE`
- `LICENSE-Apache-2.0`
- `LICENSE-CC-BY-SA-4.0`

## Attribution

Project ancestry and major upstream influences include:

- HeliBoard
- OpenBoard
- AOSP LatinIME
- FlorisBoard-inspired layout parsing work used in parts of the codebase

Kamelot keeps that technical lineage while pushing the product toward a different destination.
