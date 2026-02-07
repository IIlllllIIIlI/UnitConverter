package com.convertly.app.data.model

data class ConversionHistory(
    val id: Long = System.currentTimeMillis(),
    val fromUnit: UnitType,
    val toUnit: UnitType,
    val fromValue: Double,
    val toValue: Double,
    val category: UnitCategory,
    val timestamp: Long = System.currentTimeMillis()
)

data class FavoriteConversion(
    val fromUnit: UnitType,
    val toUnit: UnitType,
    val category: UnitCategory
)
