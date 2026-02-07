package com.convertly.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.convertly.app.data.model.UnitCategory
import com.convertly.app.data.model.UnitType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "convertly_prefs")

class PreferencesRepository(private val context: Context) {

    companion object {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
        val DECIMAL_PLACES = intPreferencesKey("decimal_places")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val SOUND_EFFECTS = booleanPreferencesKey("sound_effects")
        val ADS_REMOVED = booleanPreferencesKey("ads_removed")
        val FAVORITES = stringPreferencesKey("favorites")
        val HISTORY = stringPreferencesKey("history")
        val LAST_CATEGORY = stringPreferencesKey("last_category")
        val THOUSAND_SEPARATOR = booleanPreferencesKey("thousand_separator")
    }

    // Theme mode: "system", "light", "dark"
    val themeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[THEME_MODE] ?: "system"
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[THEME_MODE] = mode
        }
    }

    // Accent color
    val accentColor: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[ACCENT_COLOR] ?: "purple"
    }

    suspend fun setAccentColor(color: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCENT_COLOR] = color
        }
    }

    // Decimal places (0-10)
    val decimalPlaces: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[DECIMAL_PLACES] ?: 4
    }

    suspend fun setDecimalPlaces(places: Int) {
        context.dataStore.edit { prefs ->
            prefs[DECIMAL_PLACES] = places.coerceIn(0, 10)
        }
    }

    // Haptic feedback
    val hapticFeedback: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[HAPTIC_FEEDBACK] ?: true
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[HAPTIC_FEEDBACK] = enabled
        }
    }

    // Sound effects
    val soundEffects: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[SOUND_EFFECTS] ?: false
    }

    suspend fun setSoundEffects(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SOUND_EFFECTS] = enabled
        }
    }

    // Ads removed
    val adsRemoved: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ADS_REMOVED] ?: false
    }

    suspend fun setAdsRemoved(removed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ADS_REMOVED] = removed
        }
    }

    // Thousand separator
    val thousandSeparator: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[THOUSAND_SEPARATOR] ?: true
    }

    suspend fun setThousandSeparator(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THOUSAND_SEPARATOR] = enabled
        }
    }

    // Last selected category
    val lastCategory: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[LAST_CATEGORY] ?: UnitCategory.LENGTH.name
    }

    suspend fun setLastCategory(category: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_CATEGORY] = category
        }
    }

    // Favorites (stored as serialized string)
    val favorites: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[FAVORITES] ?: ""
    }

    suspend fun setFavorites(favoritesJson: String) {
        context.dataStore.edit { prefs ->
            prefs[FAVORITES] = favoritesJson
        }
    }

    // History (stored as serialized string)
    val history: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[HISTORY] ?: ""
    }

    suspend fun setHistory(historyJson: String) {
        context.dataStore.edit { prefs ->
            prefs[HISTORY] = historyJson
        }
    }

    // Per-category last used units
    fun lastFromUnit(category: UnitCategory): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[stringPreferencesKey("last_from_${category.name}")]
        }
    }

    fun lastToUnit(category: UnitCategory): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[stringPreferencesKey("last_to_${category.name}")]
        }
    }

    suspend fun setLastUnits(category: UnitCategory, from: UnitType, to: UnitType) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey("last_from_${category.name}")] = from.name
            prefs[stringPreferencesKey("last_to_${category.name}")] = to.name
        }
    }
}
