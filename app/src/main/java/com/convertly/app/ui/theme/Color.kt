package com.convertly.app.ui.theme

import androidx.compose.ui.graphics.Color

// Light theme colors
val Purple40 = Color(0xFF6750A4)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)

// Dark theme colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

// Accent color options
data class AccentColorOption(
    val name: String,
    val displayName: String,
    val light: Color,
    val dark: Color
)

val accentColors = listOf(
    AccentColorOption("purple", "Purple", Color(0xFF6750A4), Color(0xFFD0BCFF)),
    AccentColorOption("blue", "Blue", Color(0xFF1976D2), Color(0xFF90CAF9)),
    AccentColorOption("teal", "Teal", Color(0xFF00897B), Color(0xFF80CBC4)),
    AccentColorOption("green", "Green", Color(0xFF388E3C), Color(0xFFA5D6A7)),
    AccentColorOption("orange", "Orange", Color(0xFFF57C00), Color(0xFFFFCC80)),
    AccentColorOption("red", "Red", Color(0xFFD32F2F), Color(0xFFEF9A9A)),
    AccentColorOption("pink", "Pink", Color(0xFFC2185B), Color(0xFFF48FB1)),
    AccentColorOption("indigo", "Indigo", Color(0xFF303F9F), Color(0xFF9FA8DA)),
)

fun getAccentColor(name: String, isDark: Boolean): Color {
    val option = accentColors.find { it.name == name } ?: accentColors[0]
    return if (isDark) option.dark else option.light
}
