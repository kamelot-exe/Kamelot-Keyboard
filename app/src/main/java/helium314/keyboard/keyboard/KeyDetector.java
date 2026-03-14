/*
 * Copyright (C) 2010 The Android Open Source Project
 * modified
 * SPDX-License-Identifier: Apache-2.0 AND GPL-3.0-only
 */

package helium314.keyboard.keyboard;

import androidx.annotation.Nullable;

import helium314.keyboard.kamelot.layout.AdaptiveHexTouchMapper;
import helium314.keyboard.kamelot.layout.HexTouchMapper;
import helium314.keyboard.kamelot.layout.HexSwipePathResolver;

/**
 * This class handles key detection.
 */
public class KeyDetector {
    private final int mKeyHysteresisDistanceSquared;
    private final int mKeyHysteresisDistanceForSlidingModifierSquared;

    private Keyboard mKeyboard;
    private int mCorrectionX;
    private int mCorrectionY;
    @Nullable
    private HexTouchMapper mHexTouchMapper;
    @Nullable
    private HexSwipePathResolver mHexSwipePathResolver;

    public KeyDetector() {
        this(0.0f /* keyHysteresisDistance */, 0.0f /* keyHysteresisDistanceForSlidingModifier */);
    }

    /**
     * Key detection object constructor with key hysteresis distances.
     *
     * @param keyHysteresisDistance if the pointer movement distance is smaller than this, the
     * movement will not be handled as meaningful movement. The unit is pixel.
     * @param keyHysteresisDistanceForSlidingModifier the same parameter for sliding input that
     * starts from a modifier key such as shift and symbols key.
     */
    public KeyDetector(final float keyHysteresisDistance,
            final float keyHysteresisDistanceForSlidingModifier) {
        mKeyHysteresisDistanceSquared = (int)(keyHysteresisDistance * keyHysteresisDistance);
        mKeyHysteresisDistanceForSlidingModifierSquared = (int)(
                keyHysteresisDistanceForSlidingModifier * keyHysteresisDistanceForSlidingModifier);
    }

    public void setKeyboard(final Keyboard keyboard, final float correctionX,
            final float correctionY) {
        if (keyboard == null) {
            throw new NullPointerException();
        }
        mCorrectionX = (int)correctionX;
        mCorrectionY = (int)correctionY;
        mKeyboard = keyboard;
    }

    public void setHexTouchMapper(@Nullable final HexTouchMapper hexTouchMapper) {
        mHexTouchMapper = hexTouchMapper;
    }

    public void setHexSwipePathResolver(@Nullable final HexSwipePathResolver hexSwipePathResolver) {
        mHexSwipePathResolver = hexSwipePathResolver;
    }

    public void resetHexSwipePathResolver() {
        final HexSwipePathResolver hexSwipePathResolver = mHexSwipePathResolver;
        if (hexSwipePathResolver != null) {
            hexSwipePathResolver.reset();
        }
    }

    public boolean resolveGesturePoint(final int x, final int y, final int[] outCoords) {
        final HexSwipePathResolver hexSwipePathResolver = mHexSwipePathResolver;
        if (hexSwipePathResolver == null) {
            return false;
        }
        final int touchX = getTouchX(x);
        final int touchY = getTouchY(y);
        final HexSwipePathResolver.ResolvedPoint resolvedPoint =
                hexSwipePathResolver.resolve(touchX, touchY);
        outCoords[0] = resolvedPoint.getX() - mCorrectionX;
        outCoords[1] = resolvedPoint.getY() - mCorrectionY;
        return resolvedPoint.getX() != touchX || resolvedPoint.getY() != touchY;
    }

    public void recordAdaptiveHit(@Nullable final Key key, final int x, final int y) {
        if (key == null) {
            return;
        }
        final HexTouchMapper hexTouchMapper = mHexTouchMapper;
        if (hexTouchMapper instanceof AdaptiveHexTouchMapper) {
            final AdaptiveHexTouchMapper adaptiveHexTouchMapper =
                    (AdaptiveHexTouchMapper) hexTouchMapper;
            adaptiveHexTouchMapper.recordSuccessfulTouch(key, getTouchX(x), getTouchY(y));
        }
    }

    public int getKeyHysteresisDistanceSquared(final boolean isSlidingFromModifier) {
        return isSlidingFromModifier
                ? mKeyHysteresisDistanceForSlidingModifierSquared : mKeyHysteresisDistanceSquared;
    }

    public int getTouchX(final int x) {
        return x + mCorrectionX;
    }

    // TODO: Remove vertical correction.
    public int getTouchY(final int y) {
        return y + mCorrectionY;
    }

    public Keyboard getKeyboard() {
        return mKeyboard;
    }

    public boolean alwaysAllowsKeySelectionByDraggingFinger() {
        return false;
    }

    /**
     * Detect the key whose hitbox the touch point is in.
     *
     * @param x The x-coordinate of a touch point
     * @param y The y-coordinate of a touch point
     * @return the key that the touch point hits.
     */
    public Key detectHitKey(final int x, final int y) {
        if (mKeyboard == null) {
            return null;
        }
        final int touchX = getTouchX(x);
        final int touchY = getTouchY(y);
        final HexTouchMapper hexTouchMapper = mHexTouchMapper;
        if (hexTouchMapper != null) {
            final Key hexKey = hexTouchMapper.detectHitKey(touchX, touchY);
            if (hexKey != null) {
                return hexKey;
            }
        }

        int minDistance = Integer.MAX_VALUE;
        Key primaryKey = null;
        for (final Key key: mKeyboard.getNearestKeys(touchX, touchY)) {
            // An edge key always has its enlarged hitbox to respond to an event that occurred in
            // the empty area around the key. (@see Key#markAsLeftEdge(KeyboardParams)} etc.)
            if (!key.isOnKey(touchX, touchY)) {
                continue;
            }
            final int distance = key.squaredDistanceToEdge(touchX, touchY);
            if (distance > minDistance) {
                continue;
            }
            // To take care of hitbox overlaps, we compare key's code here too.
            if (primaryKey == null || distance < minDistance
                    || key.getCode() > primaryKey.getCode()) {
                minDistance = distance;
                primaryKey = key;
            }
        }
        return primaryKey;
    }
}
