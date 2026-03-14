# Keyboard Visual Redesign Notes

## Scope

The visual redesign in this pass focused on safe product-level upgrades rather than a risky renderer rewrite.

The goal was to make Kamelot feel more intentional and premium without destabilizing the inherited IME runtime.

## What Changed

### Stronger Preset Identity

Kamelot now has clearer built-in visual identities:

- Classic
- Brutal Black
- Glass Neon
- Minimal Dark
- High Contrast

These are exposed through a dedicated Theme Studio screen instead of being buried as raw theme toggles.

### Better Product-Level Visual Framing

The settings experience now visually supports the keyboard identity:

- dashboard-style landing screen
- modular cards instead of flat dumping lists
- section hero blocks
- profile cards
- theme preset cards
- clearer badges and summaries

This matters because the old settings stack made the product feel older than the runtime actually was.

### Profile-Linked Visual Recipes

Profiles now carry a dedicated appearance config alongside the theme preset. This means visual intent can travel with:

- theme preset
- border feel
- corner feel
- accent direction
- spacing density
- glow preference

Even where the renderer is not yet fully consuming every value, the product model is now coherent.

### Experimental Layout Visual Support

The hex layout path already has:

- custom hex geometry
- hex key clipping
- hex outline drawing
- adaptive hit and predictive swipe add-ons

So experimental layout work now at least looks deliberate rather than like an accidental overlay.

## What Is Intentionally Deferred

Not fully implemented yet:

- live miniature keyboard preview in settings
- direct renderer use of glow intensity
- direct renderer use of corner style overrides
- fully custom key spacing derived from appearance config
- richer pressed-state art direction
- unified custom token pipeline for every key surface

These are good follow-up targets now that the appearance model exists.

## Extension Strategy

The next safe visual steps should build on the current structure:

1. Use `KamelotAppearanceConfig` to drive more of `KeyboardTheme`.
2. Add richer preview surfaces inside the Theme Studio screen.
3. Bring spacing density and text scale into selected renderer calculations.
4. Add dedicated high-contrast and one-hand visual tuning where helpful.

## Guiding Principle

Kamelot should not look like a generic old Android keyboard with a different name. The redesign moves the product in that direction now through presets, hierarchy, and modular product framing, while keeping the stable typing path intact.
