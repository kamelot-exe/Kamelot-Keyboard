# Kamelot Appearance System Notes

## Purpose

Kamelot now has a dedicated appearance layer on top of the inherited HeliBoard theme engine. The immediate goal is not to replace the renderer, but to give the product a stronger preset system, profile-linked appearance data, and a clearer path toward deeper visual customization.

## Built-In Presets

Current curated presets:

1. Classic
2. Brutal Black
3. Glass Neon
4. Minimal Dark
5. High Contrast

These presets map onto the existing `KeyboardTheme` stack through `ThemePresetMapper`, so they remain compatible with the stable runtime.

## Safe Runtime Wiring

Already wired safely:

- theme preset selection
- mapping preset to base style
- mapping preset to day/night theme colors
- mapping preset to key border behavior
- profile-linked preset persistence
- theme reload through existing `KeyboardSwitcher` hooks

## Appearance Config Model

Profiles now also carry a `KamelotAppearanceConfig`, which stores a product-facing appearance recipe independent from the old raw theme preferences.

Current config dimensions:

- accent palette
- key background style
- border style
- corner style
- glow intensity
- text scale
- spacing density

This model is intentionally broader than what the renderer can fully express today. The point is to establish a stable customization contract now.

## What Is Live Today

Live and user-facing now:

- preset card selection in the Kamelot themes screen
- active preset highlighting
- preset descriptions
- profile-aware preset storage
- profile-aware appearance config storage
- appearance controls for accent/background/border/corner/glow/spacing recipes

## Renderer Hooks Already Used

The current renderer still relies on the inherited theme system for actual drawing, but the following paths already matter:

- rounded versus material base style
- day/night theme color selection
- border enablement
- hex outline behavior already layered in experimental layout mode

This means Kamelot can improve product feel immediately without destabilizing key drawing.

## Deferred Renderer Hooks

Not fully wired yet and intentionally deferred:

- glow intensity affecting real key lighting
- corner style overriding drawable geometry directly
- spacing density automatically rewriting key gaps
- accent palette driving a full custom color pipeline
- rich live keyboard preview component inside settings

These are now safe next steps because the config model and settings surface already exist.

## Product Direction

The appearance system is designed around a premium dark-first identity:

- Brutal Black for sharp minimalism
- Glass Neon for expressive visual richness
- Minimal Dark for low-glare focus
- High Contrast for readability and stronger separation

The result is a more deliberate visual language than the inherited stock settings flow.
