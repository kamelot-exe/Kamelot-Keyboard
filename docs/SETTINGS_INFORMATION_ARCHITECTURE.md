# Kamelot Keyboard Settings Information Architecture

## Goal

The settings system now behaves like a product UI instead of a flat legacy preference dump. The redesign keeps the existing stable settings backend and search index, but wraps it in a clearer information architecture built around high-level user tasks.

## Top-Level Sections

The main settings landing screen is now organized into ten product sections:

1. Appearance
2. Layout & Typing
3. Profiles
4. Gestures
5. Actions & Toolbar
6. Macros & Clipboard
7. Languages
8. Privacy & Data
9. Experimental
10. About / Advanced

Each section is presented as a large dashboard card and opens into a dedicated section screen with:

- a short overview
- links to deeper leaf screens
- the most relevant inline controls
- preserved global search access

## Design Approach

The redesign deliberately does not replace the stable settings container or search pipeline. Instead it adds a product-oriented navigation layer on top of:

- `SettingsContainer`
- `SearchSettingsScreen`
- existing stable screens such as `AppearanceScreen`, `PreferencesScreen`, `ToolbarScreen`, `LanguageScreen`, and `AdvancedSettingsScreen`
- Kamelot-specific screens for profiles, themes, quick actions, macros, Gesture OS, and experimental systems

This keeps risk low while improving discoverability.

## Navigation Model

### Landing Screen

`MainSettingsScreen` is now a modular dashboard with:

- a hero summary
- ten top-level section cards
- one lightweight gesture shortcut card
- persistent global-search hinting

### Section Screens

Each top-level section uses a section-hub pattern:

- overview card
- curated navigation cards
- inline high-signal settings
- built-in global search via the existing search scaffold

This avoids giant walls of toggles while still preserving direct access to important controls.

## Section Responsibilities

### Appearance

- theme presets
- renderer appearance
- theme style
- color themes
- borders
- font scale
- keyboard sizing
- spacing

### Layout & Typing

- layout mode
- number row
- language switch key
- popup preview
- vibration
- sound
- text correction access
- secondary layouts

### Profiles

- active profile
- profile-linked layout mode
- profile-linked theme preset
- profile routing groundwork

### Gestures

- stock gesture typing
- Gesture OS
- gesture experiment toggles
- predictive hex swipe path hook

### Actions & Toolbar

- stock toolbar
- Kamelot quick panel
- macro strip gate
- action diagnostics

### Macros & Clipboard

- macro manager
- clipboard history
- retention
- pinned-first behavior

### Languages

- enabled languages
- dictionaries
- secondary layouts
- switching behavior

### Privacy & Data

- local-only controls
- clipboard retention visibility
- adaptive hit reset
- incognito behavior
- module-level disable paths

### Experimental

- experimental features master gate
- hex layout
- adaptive hex hit zones
- predictive hex swipe path

### About / Advanced

- advanced settings
- system-level tuning
- project info

## Search

The redesign preserves the strongest part of the old settings stack: global search.

Search still works against the full registered settings container, not only the currently visible screen. This means:

- users can discover buried controls quickly
- modular navigation does not fragment findability
- future Kamelot modules can be added without rebuilding search architecture

## Modularity

Kamelot-specific modules can now be enabled or disabled independently through the module manager. Current runtime-facing module gates include:

- profiles
- themes
- quick panel
- macros
- Gesture OS
- hex lab

This modular approach is intended to scale into a fuller builder-style product later.

## Why This Structure

The redesign optimizes for three user types at once:

- normal users who want obvious sections and clean defaults
- advanced users who want direct control and search
- future Kamelot development, which needs room for profiles, gestures, actions, and experimental layouts without collapsing into a single junk drawer

## Extension Points

Next safe expansions should land inside this IA rather than creating new isolated screens:

- richer Appearance preset previews
- custom profile creation and duplication
- deeper Quick Panel builder controls
- clearer Privacy explanations and reset flows
- app-aware context routing when safe
