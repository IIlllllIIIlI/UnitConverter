package com.convertly.app.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class UnitCategory(
    val displayName: String,
    val icon: ImageVector,
    val color: Color,
    val units: List<UnitType>
) {
    LENGTH(
        displayName = "Length",
        icon = Icons.Outlined.Straighten,
        color = Color(0xFF4CAF50),
        units = listOf(
            UnitType.MILLIMETER, UnitType.CENTIMETER, UnitType.METER,
            UnitType.KILOMETER, UnitType.INCH, UnitType.FOOT,
            UnitType.YARD, UnitType.MILE, UnitType.NAUTICAL_MILE,
            UnitType.MICROMETER
        )
    ),
    WEIGHT(
        displayName = "Weight",
        icon = Icons.Outlined.FitnessCenter,
        color = Color(0xFF2196F3),
        units = listOf(
            UnitType.MILLIGRAM, UnitType.GRAM, UnitType.KILOGRAM,
            UnitType.METRIC_TON, UnitType.OUNCE, UnitType.POUND,
            UnitType.STONE, UnitType.US_TON, UnitType.IMPERIAL_TON
        )
    ),
    TEMPERATURE(
        displayName = "Temperature",
        icon = Icons.Outlined.Thermostat,
        color = Color(0xFFFF5722),
        units = listOf(
            UnitType.CELSIUS, UnitType.FAHRENHEIT, UnitType.KELVIN, UnitType.RANKINE
        )
    ),
    CURRENCY(
        displayName = "Currency",
        icon = Icons.Outlined.AttachMoney,
        color = Color(0xFFFFC107),
        units = listOf(
            UnitType.USD, UnitType.EUR, UnitType.GBP, UnitType.JPY,
            UnitType.CAD, UnitType.AUD, UnitType.CHF, UnitType.CNY,
            UnitType.INR, UnitType.MXN, UnitType.BRL, UnitType.KRW,
            UnitType.SEK, UnitType.NOK, UnitType.NZD, UnitType.SGD
        )
    ),
    VOLUME(
        displayName = "Volume",
        icon = Icons.Outlined.LocalDrink,
        color = Color(0xFF9C27B0),
        units = listOf(
            UnitType.MILLILITER, UnitType.LITER, UnitType.CUBIC_METER,
            UnitType.TEASPOON, UnitType.TABLESPOON, UnitType.FLUID_OUNCE,
            UnitType.CUP, UnitType.PINT, UnitType.QUART, UnitType.GALLON
        )
    ),
    AREA(
        displayName = "Area",
        icon = Icons.Outlined.CropSquare,
        color = Color(0xFF795548),
        units = listOf(
            UnitType.SQ_MILLIMETER, UnitType.SQ_CENTIMETER, UnitType.SQ_METER,
            UnitType.SQ_KILOMETER, UnitType.HECTARE, UnitType.ACRE,
            UnitType.SQ_INCH, UnitType.SQ_FOOT, UnitType.SQ_YARD, UnitType.SQ_MILE
        )
    ),
    SPEED(
        displayName = "Speed",
        icon = Icons.Outlined.Speed,
        color = Color(0xFF00BCD4),
        units = listOf(
            UnitType.METERS_PER_SEC, UnitType.KM_PER_HOUR, UnitType.MILES_PER_HOUR,
            UnitType.KNOT, UnitType.FEET_PER_SEC, UnitType.MACH
        )
    ),
    TIME(
        displayName = "Time",
        icon = Icons.Outlined.Schedule,
        color = Color(0xFFE91E63),
        units = listOf(
            UnitType.MILLISECOND, UnitType.SECOND, UnitType.MINUTE,
            UnitType.HOUR, UnitType.DAY, UnitType.WEEK,
            UnitType.MONTH, UnitType.YEAR
        )
    ),
    DATA(
        displayName = "Data",
        icon = Icons.Outlined.Storage,
        color = Color(0xFF607D8B),
        units = listOf(
            UnitType.BIT, UnitType.BYTE, UnitType.KILOBYTE,
            UnitType.MEGABYTE, UnitType.GIGABYTE, UnitType.TERABYTE,
            UnitType.PETABYTE, UnitType.KIBIBYTE, UnitType.MEBIBYTE,
            UnitType.GIBIBYTE, UnitType.TEBIBYTE
        )
    ),
    PRESSURE(
        displayName = "Pressure",
        icon = Icons.Outlined.Compress,
        color = Color(0xFF3F51B5),
        units = listOf(
            UnitType.PASCAL, UnitType.KILOPASCAL, UnitType.BAR,
            UnitType.PSI, UnitType.ATM, UnitType.MMHG, UnitType.TORR
        )
    ),
    ENERGY(
        displayName = "Energy",
        icon = Icons.Outlined.Bolt,
        color = Color(0xFFFF9800),
        units = listOf(
            UnitType.JOULE, UnitType.KILOJOULE, UnitType.CALORIE,
            UnitType.KILOCALORIE, UnitType.WATT_HOUR, UnitType.KILOWATT_HOUR,
            UnitType.BTU, UnitType.ELECTRONVOLT
        )
    ),
    POWER(
        displayName = "Power",
        icon = Icons.Outlined.FlashOn,
        color = Color(0xFF8BC34A),
        units = listOf(
            UnitType.WATT, UnitType.KILOWATT, UnitType.MEGAWATT,
            UnitType.HORSEPOWER, UnitType.BTU_PER_HOUR,
            UnitType.FOOT_POUND_PER_SEC
        )
    );
}
