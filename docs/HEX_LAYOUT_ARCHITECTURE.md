# Hex Layout Architecture

Phase 7 introduces a feature-gated hex layout layer on top of the existing HeliBoard-derived keyboard engine. Standard rectangular layouts remain the default path.

## Geometry Model

- `HexKeyGeometry` stores a cached pointy-top hex polygon for a `Key`.
- `HexGridLayout` derives a geometry map from the current keyboard keys and optional layout metadata.
- `HexTouchMapper` resolves touch points against hex polygons instead of rectangular hit boxes.

The current implementation derives hex geometry from existing key bounds. This keeps the runtime stable while preparing a future authored hex layout format.

## Layout Metadata

`LayoutParser` now accepts two JSON shapes:

- legacy top-level arrays of rows
- object-wrapped layouts with:
  - `rows` or `layout`
  - `layoutMode`
  - `hexRadius`
  - `hexSpacing`
  - `rowOffset`

If metadata is absent, Kamelot falls back to conservative derived hex values.

## Rendering Pipeline

- `KeyboardView` still owns the draw loop.
- When the active Kamelot profile resolves to `HEX_EXPERIMENTAL` and the feature flag is enabled, key backgrounds are clipped to cached hex paths.
- Labels and icons continue to use the existing rendering logic to avoid destabilizing the keyboard visuals.

This keeps hex mode additive and leaves standard mode untouched.

## Touch Mapping

- `KeyDetector` now supports an optional `HexTouchMapper`.
- Hex mode checks polygon hits first.
- If no hex polygon resolves the touch, the stock rectangular key detection path runs unchanged.

This preserves normal typing behavior and avoids JNI or gesture typing changes.

## Limitations

- Preview popups still use stock placement and shape handling.
- Proximity info remains rectangular and unchanged.
- Current hex mode is derived from existing key bounds, not from a dedicated hex-authored asset pipeline.

## Safe Extension Points

- Add dedicated hex-authored layouts later through the new metadata wrapper.
- Move per-profile hex spacing and row offset into Keyboard Builder controls.
- Add hex-aware preview placement only after geometry proves stable.
- Keep future gesture integration routed after touch-to-key mapping rather than rewriting the gesture engine.
