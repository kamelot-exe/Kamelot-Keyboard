# Hex Usability Enhancements

This add-on strengthens Phase 7 hex layout usability in two narrow, feature-gated ways:

- adaptive hit zones
- predictive hex swipe path

Both systems are local-only and disabled by default.

## Adaptive Hit Zones

Relevant classes:

- `AdaptiveHitStats`
- `AdaptiveHitModel`
- `AdaptiveHexTouchMapper`

How it works:

- Successful hex taps record a small offset from the key center.
- Stats are stored locally in shared preferences as lightweight JSON.
- Adaptation activates only after a minimum sample count.
- Offset bias is clamped to a small fraction of the hex radius.
- Visible key geometry does not change. Only hit testing is biased.

Safety properties:

- local-only storage
- bounded correction
- safe fallback to normal hex hit testing when data is missing or insufficient
- reset path exposed in Kamelot settings

## Predictive Hex Swipe Path

Relevant classes:

- `HexSwipePointSnapper`
- `HexSwipePathResolver`
- `HexSwipeTrailModel`

How it works:

- Raw gesture points are preprocessed before entering the existing batch-input path.
- Points are lightly pulled toward plausible nearby hex centers.
- Short oscillation between neighboring hexes is suppressed.
- The cleaned path is then passed into the existing gesture pipeline.

This is intentionally not a gesture-engine rewrite. It is a small preprocessing layer that only activates for experimental hex layouts.

## Feature Gates

These systems require:

- experimental features enabled
- hex layout enabled
- per-feature toggle enabled

Feature flags:

- `kamelot_enable_adaptive_hex_hit_zones`
- `kamelot_enable_predictive_hex_swipe_path`

## Rectangular Layout Stability

- Standard layout mode does not instantiate these systems.
- Default rectangular hit detection remains unchanged.
- Existing swipe typing remains the fallback when hex preprocessing is inactive.

## Deferred

- per-user adaptive decay or time-based weighting
- hex-native preview rendering
- richer path smoothing based on authored hex layouts
- JNI-level proximity changes
- dual-layer or split-key behavior
