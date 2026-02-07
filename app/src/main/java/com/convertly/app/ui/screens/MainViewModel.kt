package com.convertly.app.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.convertly.app.data.model.ConversionHistory
import com.convertly.app.data.model.FavoriteConversion
import com.convertly.app.data.model.UnitCategory
import com.convertly.app.data.model.UnitType
import com.convertly.app.data.repository.PreferencesRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepo = PreferencesRepository(application)
    private val gson = Gson()

    // Settings
    val themeMode = prefsRepo.themeMode.stateIn(viewModelScope, SharingStarted.Eagerly, "system")
    val accentColor = prefsRepo.accentColor.stateIn(viewModelScope, SharingStarted.Eagerly, "purple")
    val decimalPlaces = prefsRepo.decimalPlaces.stateIn(viewModelScope, SharingStarted.Eagerly, 4)
    val hapticFeedback = prefsRepo.hapticFeedback.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val soundEffects = prefsRepo.soundEffects.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val adsRemoved = prefsRepo.adsRemoved.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val thousandSeparator = prefsRepo.thousandSeparator.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Converter state
    private val _selectedCategory = MutableStateFlow(UnitCategory.LENGTH)
    val selectedCategory: StateFlow<UnitCategory> = _selectedCategory.asStateFlow()

    private val _fromUnit = MutableStateFlow(UnitType.METER)
    val fromUnit: StateFlow<UnitType> = _fromUnit.asStateFlow()

    private val _toUnit = MutableStateFlow(UnitType.FOOT)
    val toUnit: StateFlow<UnitType> = _toUnit.asStateFlow()

    private val _inputValue = MutableStateFlow("")
    val inputValue: StateFlow<String> = _inputValue.asStateFlow()

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    // History
    private val _history = MutableStateFlow<List<ConversionHistory>>(emptyList())
    val history: StateFlow<List<ConversionHistory>> = _history.asStateFlow()

    // Favorites
    private val _favorites = MutableStateFlow<List<FavoriteConversion>>(emptyList())
    val favorites: StateFlow<List<FavoriteConversion>> = _favorites.asStateFlow()

    init {
        loadHistory()
        loadFavorites()
        loadLastCategory()
    }

    fun selectCategory(category: UnitCategory) {
        _selectedCategory.value = category
        _fromUnit.value = category.units[0]
        _toUnit.value = if (category.units.size > 1) category.units[1] else category.units[0]
        _inputValue.value = ""
        _result.value = ""

        viewModelScope.launch {
            prefsRepo.setLastCategory(category.name)
            // Load last used units for this category
            prefsRepo.lastFromUnit(category).first()?.let { unitName ->
                try {
                    _fromUnit.value = UnitType.valueOf(unitName)
                } catch (_: Exception) {}
            }
            prefsRepo.lastToUnit(category).first()?.let { unitName ->
                try {
                    _toUnit.value = UnitType.valueOf(unitName)
                } catch (_: Exception) {}
            }
        }
    }

    fun setFromUnit(unit: UnitType) {
        _fromUnit.value = unit
        convert()
        saveLastUnits()
    }

    fun setToUnit(unit: UnitType) {
        _toUnit.value = unit
        convert()
        saveLastUnits()
    }

    fun setInputValue(value: String) {
        // Allow digits, decimal point, negative sign
        val filtered = value.filter { it.isDigit() || it == '.' || it == '-' }
        // Prevent multiple decimal points
        val dotCount = filtered.count { it == '.' }
        if (dotCount > 1) return
        // Prevent negative sign anywhere except start
        val minusCount = filtered.count { it == '-' }
        if (minusCount > 1) return
        if (minusCount == 1 && filtered.indexOf('-') != 0) return

        _inputValue.value = filtered
        convert()
    }

    fun swapUnits() {
        val tempFrom = _fromUnit.value
        _fromUnit.value = _toUnit.value
        _toUnit.value = tempFrom
        convert()
        saveLastUnits()
    }

    private fun convert() {
        val input = _inputValue.value.toDoubleOrNull()
        if (input == null) {
            _result.value = ""
            return
        }

        val converted = UnitType.convert(input, _fromUnit.value, _toUnit.value)
        val decimals = decimalPlaces.value
        _result.value = formatNumber(converted, decimals)
    }

    private fun formatNumber(value: Double, decimals: Int): String {
        val formatted = "%.${decimals}f".format(value)
        if (!thousandSeparator.value) return formatted

        val parts = formatted.split(".")
        val intPart = parts[0].reversed().chunked(3).joinToString(",").reversed()
        return if (parts.size > 1) "$intPart.${parts[1]}" else intPart
    }

    fun addToHistory() {
        val input = _inputValue.value.toDoubleOrNull() ?: return
        val converted = UnitType.convert(input, _fromUnit.value, _toUnit.value)

        val entry = ConversionHistory(
            fromUnit = _fromUnit.value,
            toUnit = _toUnit.value,
            fromValue = input,
            toValue = converted,
            category = _selectedCategory.value
        )

        val currentHistory = _history.value.toMutableList()
        currentHistory.add(0, entry)
        // Keep last 50 entries
        if (currentHistory.size > 50) {
            currentHistory.removeAt(currentHistory.lastIndex)
        }
        _history.value = currentHistory
        saveHistory()
    }

    fun clearHistory() {
        _history.value = emptyList()
        saveHistory()
    }

    fun toggleFavorite(from: UnitType, to: UnitType, category: UnitCategory) {
        val fav = FavoriteConversion(from, to, category)
        val currentFavorites = _favorites.value.toMutableList()
        val existing = currentFavorites.find {
            it.fromUnit == from && it.toUnit == to && it.category == category
        }
        if (existing != null) {
            currentFavorites.remove(existing)
        } else {
            currentFavorites.add(fav)
        }
        _favorites.value = currentFavorites
        saveFavorites()
    }

    fun isFavorite(from: UnitType, to: UnitType, category: UnitCategory): Boolean {
        return _favorites.value.any {
            it.fromUnit == from && it.toUnit == to && it.category == category
        }
    }

    fun loadFavoriteConversion(fav: FavoriteConversion) {
        selectCategory(fav.category)
        _fromUnit.value = fav.fromUnit
        _toUnit.value = fav.toUnit
    }

    // Settings mutations
    fun setThemeMode(mode: String) = viewModelScope.launch { prefsRepo.setThemeMode(mode) }
    fun setAccentColor(color: String) = viewModelScope.launch { prefsRepo.setAccentColor(color) }
    fun setDecimalPlaces(places: Int) = viewModelScope.launch {
        prefsRepo.setDecimalPlaces(places)
        convert()
    }
    fun setHapticFeedback(enabled: Boolean) = viewModelScope.launch { prefsRepo.setHapticFeedback(enabled) }
    fun setSoundEffects(enabled: Boolean) = viewModelScope.launch { prefsRepo.setSoundEffects(enabled) }
    fun setAdsRemoved(removed: Boolean) = viewModelScope.launch { prefsRepo.setAdsRemoved(removed) }
    fun setThousandSeparator(enabled: Boolean) = viewModelScope.launch {
        prefsRepo.setThousandSeparator(enabled)
        convert()
    }

    private fun saveLastUnits() {
        viewModelScope.launch {
            prefsRepo.setLastUnits(_selectedCategory.value, _fromUnit.value, _toUnit.value)
        }
    }

    private fun loadLastCategory() {
        viewModelScope.launch {
            val categoryName = prefsRepo.lastCategory.first()
            try {
                val category = UnitCategory.valueOf(categoryName)
                selectCategory(category)
            } catch (_: Exception) {}
        }
    }

    // Serialization helpers for history
    private fun saveHistory() {
        viewModelScope.launch {
            val serialized = _history.value.map { entry ->
                mapOf(
                    "id" to entry.id,
                    "fromUnit" to entry.fromUnit.name,
                    "toUnit" to entry.toUnit.name,
                    "fromValue" to entry.fromValue,
                    "toValue" to entry.toValue,
                    "category" to entry.category.name,
                    "timestamp" to entry.timestamp
                )
            }
            prefsRepo.setHistory(gson.toJson(serialized))
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            val json = prefsRepo.history.first()
            if (json.isBlank()) return@launch
            try {
                val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                val list: List<Map<String, Any>> = gson.fromJson(json, type)
                _history.value = list.mapNotNull { map ->
                    try {
                        ConversionHistory(
                            id = (map["id"] as Double).toLong(),
                            fromUnit = UnitType.valueOf(map["fromUnit"] as String),
                            toUnit = UnitType.valueOf(map["toUnit"] as String),
                            fromValue = map["fromValue"] as Double,
                            toValue = map["toValue"] as Double,
                            category = UnitCategory.valueOf(map["category"] as String),
                            timestamp = (map["timestamp"] as Double).toLong()
                        )
                    } catch (_: Exception) { null }
                }
            } catch (_: Exception) {}
        }
    }

    private fun saveFavorites() {
        viewModelScope.launch {
            val serialized = _favorites.value.map { fav ->
                mapOf(
                    "fromUnit" to fav.fromUnit.name,
                    "toUnit" to fav.toUnit.name,
                    "category" to fav.category.name
                )
            }
            prefsRepo.setFavorites(gson.toJson(serialized))
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val json = prefsRepo.favorites.first()
            if (json.isBlank()) return@launch
            try {
                val type = object : TypeToken<List<Map<String, String>>>() {}.type
                val list: List<Map<String, String>> = gson.fromJson(json, type)
                _favorites.value = list.mapNotNull { map ->
                    try {
                        FavoriteConversion(
                            fromUnit = UnitType.valueOf(map["fromUnit"]!!),
                            toUnit = UnitType.valueOf(map["toUnit"]!!),
                            category = UnitCategory.valueOf(map["category"]!!)
                        )
                    } catch (_: Exception) { null }
                }
            } catch (_: Exception) {}
        }
    }
}
