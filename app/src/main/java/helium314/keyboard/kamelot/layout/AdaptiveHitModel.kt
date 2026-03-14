// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.kamelot.layout

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import helium314.keyboard.keyboard.Key
import helium314.keyboard.latin.utils.prefs
import kotlinx.serialization.json.Json
import kotlin.math.sqrt

class AdaptiveHitModel @JvmOverloads constructor(
    context: Context,
    private val prefs: SharedPreferences = context.prefs(),
) {
    companion object {
        const val PREF_ADAPTIVE_HIT_STATS_JSON = "kamelot_hex_adaptive_hit_stats_json"
        private const val MIN_SAMPLE_COUNT = 6
        private const val MAX_OFFSET_RATIO = 0.18f
        private const val MAX_PERSISTED_KEYS = 96
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private var cachedStats: MutableMap<String, AdaptiveHitStats>? = null

    fun getBiasFor(key: Key, geometry: HexKeyGeometry): Pair<Float, Float> {
        val stats = loadStats()[key.stableAdaptiveId()] ?: return 0f to 0f
        if (stats.sampleCount < MIN_SAMPLE_COUNT) return 0f to 0f
        val maxOffset = geometry.radius * MAX_OFFSET_RATIO
        val biasX = stats.averageOffsetX.coerceIn(-maxOffset, maxOffset)
        val biasY = stats.averageOffsetY.coerceIn(-maxOffset, maxOffset)
        return biasX to biasY
    }

    fun getToleranceMultiplier(key: Key, geometry: HexKeyGeometry): Float {
        val stats = loadStats()[key.stableAdaptiveId()] ?: return 1.0f
        if (stats.sampleCount < MIN_SAMPLE_COUNT) return 1.0f
        val varianceRadius = sqrt(stats.averageDistanceSquared).coerceAtMost(geometry.radius * 0.14f)
        return 1.0f + (varianceRadius / geometry.radius)
    }

    fun recordSuccessfulTouch(key: Key, geometry: HexKeyGeometry, touchX: Float, touchY: Float) {
        val keyId = key.stableAdaptiveId()
        val maxOffset = geometry.radius * MAX_OFFSET_RATIO
        val offsetX = (touchX - geometry.centerX).coerceIn(-maxOffset, maxOffset)
        val offsetY = (touchY - geometry.centerY).coerceIn(-maxOffset, maxOffset)
        val current = loadStats()[keyId] ?: AdaptiveHitStats(keyId = keyId)
        val updated = current.record(offsetX, offsetY)
        val stats = loadStats()
        stats[keyId] = updated
        trimToBudget(stats)
        persist(stats)
    }

    fun reset() {
        cachedStats = mutableMapOf()
        prefs.edit { remove(PREF_ADAPTIVE_HIT_STATS_JSON) }
    }

    private fun loadStats(): MutableMap<String, AdaptiveHitStats> {
        cachedStats?.let { return it }
        val decoded = prefs.getString(PREF_ADAPTIVE_HIT_STATS_JSON, null)
            ?.let { runCatching { json.decodeFromString<List<AdaptiveHitStats>>(it) }.getOrNull() }
            .orEmpty()
            .associateByTo(linkedMapOf()) { it.keyId }
        return decoded.also { cachedStats = it }
    }

    private fun persist(stats: Map<String, AdaptiveHitStats>) {
        cachedStats = stats.toMutableMap()
        prefs.edit {
            putString(
                PREF_ADAPTIVE_HIT_STATS_JSON,
                json.encodeToString(stats.values.sortedByDescending { it.sampleCount }.take(MAX_PERSISTED_KEYS))
            )
        }
    }

    private fun trimToBudget(stats: MutableMap<String, AdaptiveHitStats>) {
        if (stats.size <= MAX_PERSISTED_KEYS) return
        val removableKeys = stats.values
            .sortedBy { it.sampleCount }
            .take(stats.size - MAX_PERSISTED_KEYS)
            .map { it.keyId }
        removableKeys.forEach(stats::remove)
    }
}

private fun Key.stableAdaptiveId(): String = "${code}@${x},${y}"
