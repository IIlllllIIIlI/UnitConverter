package com.convertly.app.data.model

enum class UnitType(
    val displayName: String,
    val abbreviation: String,
    val toBase: (Double) -> Double,
    val fromBase: (Double) -> Double
) {
    // ===== LENGTH (base: meter) =====
    MILLIMETER("Millimeter", "mm", { it * 0.001 }, { it * 1000.0 }),
    CENTIMETER("Centimeter", "cm", { it * 0.01 }, { it * 100.0 }),
    METER("Meter", "m", { it }, { it }),
    KILOMETER("Kilometer", "km", { it * 1000.0 }, { it / 1000.0 }),
    INCH("Inch", "in", { it * 0.0254 }, { it / 0.0254 }),
    FOOT("Foot", "ft", { it * 0.3048 }, { it / 0.3048 }),
    YARD("Yard", "yd", { it * 0.9144 }, { it / 0.9144 }),
    MILE("Mile", "mi", { it * 1609.344 }, { it / 1609.344 }),
    NAUTICAL_MILE("Nautical Mile", "nmi", { it * 1852.0 }, { it / 1852.0 }),
    MICROMETER("Micrometer", "μm", { it * 1e-6 }, { it * 1e6 }),

    // ===== WEIGHT (base: kilogram) =====
    MILLIGRAM("Milligram", "mg", { it * 1e-6 }, { it * 1e6 }),
    GRAM("Gram", "g", { it * 0.001 }, { it * 1000.0 }),
    KILOGRAM("Kilogram", "kg", { it }, { it }),
    METRIC_TON("Metric Ton", "t", { it * 1000.0 }, { it / 1000.0 }),
    OUNCE("Ounce", "oz", { it * 0.0283495 }, { it / 0.0283495 }),
    POUND("Pound", "lb", { it * 0.453592 }, { it / 0.453592 }),
    STONE("Stone", "st", { it * 6.35029 }, { it / 6.35029 }),
    US_TON("US Ton", "US tn", { it * 907.185 }, { it / 907.185 }),
    IMPERIAL_TON("Imperial Ton", "imp tn", { it * 1016.05 }, { it / 1016.05 }),

    // ===== TEMPERATURE (special handling - base: Celsius) =====
    CELSIUS("Celsius", "°C", { it }, { it }),
    FAHRENHEIT("Fahrenheit", "°F", { (it - 32.0) * 5.0 / 9.0 }, { it * 9.0 / 5.0 + 32.0 }),
    KELVIN("Kelvin", "K", { it - 273.15 }, { it + 273.15 }),
    RANKINE("Rankine", "°R", { (it - 491.67) * 5.0 / 9.0 }, { it * 9.0 / 5.0 + 491.67 }),

    // ===== CURRENCY (base: USD - rates are approximate, updated manually) =====
    USD("US Dollar", "$", { it }, { it }),
    EUR("Euro", "€", { it / 0.92 }, { it * 0.92 }),
    GBP("British Pound", "£", { it / 0.79 }, { it * 0.79 }),
    JPY("Japanese Yen", "¥", { it / 149.50 }, { it * 149.50 }),
    CAD("Canadian Dollar", "CA$", { it / 1.36 }, { it * 1.36 }),
    AUD("Australian Dollar", "A$", { it / 1.53 }, { it * 1.53 }),
    CHF("Swiss Franc", "CHF", { it / 0.88 }, { it * 0.88 }),
    CNY("Chinese Yuan", "CN¥", { it / 7.24 }, { it * 7.24 }),
    INR("Indian Rupee", "₹", { it / 83.12 }, { it * 83.12 }),
    MXN("Mexican Peso", "MX$", { it / 17.15 }, { it * 17.15 }),
    BRL("Brazilian Real", "R$", { it / 4.97 }, { it * 4.97 }),
    KRW("South Korean Won", "₩", { it / 1330.0 }, { it * 1330.0 }),
    SEK("Swedish Krona", "kr", { it / 10.42 }, { it * 10.42 }),
    NOK("Norwegian Krone", "kr", { it / 10.55 }, { it * 10.55 }),
    NZD("New Zealand Dollar", "NZ$", { it / 1.63 }, { it * 1.63 }),
    SGD("Singapore Dollar", "S$", { it / 1.34 }, { it * 1.34 }),

    // ===== VOLUME (base: liter) =====
    MILLILITER("Milliliter", "mL", { it * 0.001 }, { it * 1000.0 }),
    LITER("Liter", "L", { it }, { it }),
    CUBIC_METER("Cubic Meter", "m³", { it * 1000.0 }, { it / 1000.0 }),
    TEASPOON("Teaspoon", "tsp", { it * 0.00492892 }, { it / 0.00492892 }),
    TABLESPOON("Tablespoon", "tbsp", { it * 0.0147868 }, { it / 0.0147868 }),
    FLUID_OUNCE("Fluid Ounce", "fl oz", { it * 0.0295735 }, { it / 0.0295735 }),
    CUP("Cup", "cup", { it * 0.236588 }, { it / 0.236588 }),
    PINT("Pint", "pt", { it * 0.473176 }, { it / 0.473176 }),
    QUART("Quart", "qt", { it * 0.946353 }, { it / 0.946353 }),
    GALLON("Gallon", "gal", { it * 3.78541 }, { it / 3.78541 }),

    // ===== AREA (base: square meter) =====
    SQ_MILLIMETER("Square Millimeter", "mm²", { it * 1e-6 }, { it * 1e6 }),
    SQ_CENTIMETER("Square Centimeter", "cm²", { it * 1e-4 }, { it * 1e4 }),
    SQ_METER("Square Meter", "m²", { it }, { it }),
    SQ_KILOMETER("Square Kilometer", "km²", { it * 1e6 }, { it / 1e6 }),
    HECTARE("Hectare", "ha", { it * 10000.0 }, { it / 10000.0 }),
    ACRE("Acre", "ac", { it * 4046.86 }, { it / 4046.86 }),
    SQ_INCH("Square Inch", "in²", { it * 0.00064516 }, { it / 0.00064516 }),
    SQ_FOOT("Square Foot", "ft²", { it * 0.092903 }, { it / 0.092903 }),
    SQ_YARD("Square Yard", "yd²", { it * 0.836127 }, { it / 0.836127 }),
    SQ_MILE("Square Mile", "mi²", { it * 2.59e6 }, { it / 2.59e6 }),

    // ===== SPEED (base: meters per second) =====
    METERS_PER_SEC("Meters/Second", "m/s", { it }, { it }),
    KM_PER_HOUR("Kilometers/Hour", "km/h", { it / 3.6 }, { it * 3.6 }),
    MILES_PER_HOUR("Miles/Hour", "mph", { it * 0.44704 }, { it / 0.44704 }),
    KNOT("Knot", "kn", { it * 0.514444 }, { it / 0.514444 }),
    FEET_PER_SEC("Feet/Second", "ft/s", { it * 0.3048 }, { it / 0.3048 }),
    MACH("Mach", "Ma", { it * 343.0 }, { it / 343.0 }),

    // ===== TIME (base: second) =====
    MILLISECOND("Millisecond", "ms", { it * 0.001 }, { it * 1000.0 }),
    SECOND("Second", "s", { it }, { it }),
    MINUTE("Minute", "min", { it * 60.0 }, { it / 60.0 }),
    HOUR("Hour", "h", { it * 3600.0 }, { it / 3600.0 }),
    DAY("Day", "d", { it * 86400.0 }, { it / 86400.0 }),
    WEEK("Week", "wk", { it * 604800.0 }, { it / 604800.0 }),
    MONTH("Month", "mo", { it * 2629746.0 }, { it / 2629746.0 }),
    YEAR("Year", "yr", { it * 31556952.0 }, { it / 31556952.0 }),

    // ===== DATA (base: byte) =====
    BIT("Bit", "b", { it / 8.0 }, { it * 8.0 }),
    BYTE("Byte", "B", { it }, { it }),
    KILOBYTE("Kilobyte", "KB", { it * 1000.0 }, { it / 1000.0 }),
    MEGABYTE("Megabyte", "MB", { it * 1e6 }, { it / 1e6 }),
    GIGABYTE("Gigabyte", "GB", { it * 1e9 }, { it / 1e9 }),
    TERABYTE("Terabyte", "TB", { it * 1e12 }, { it / 1e12 }),
    PETABYTE("Petabyte", "PB", { it * 1e15 }, { it / 1e15 }),
    KIBIBYTE("Kibibyte", "KiB", { it * 1024.0 }, { it / 1024.0 }),
    MEBIBYTE("Mebibyte", "MiB", { it * 1048576.0 }, { it / 1048576.0 }),
    GIBIBYTE("Gibibyte", "GiB", { it * 1073741824.0 }, { it / 1073741824.0 }),
    TEBIBYTE("Tebibyte", "TiB", { it * 1099511627776.0 }, { it / 1099511627776.0 }),

    // ===== PRESSURE (base: Pascal) =====
    PASCAL("Pascal", "Pa", { it }, { it }),
    KILOPASCAL("Kilopascal", "kPa", { it * 1000.0 }, { it / 1000.0 }),
    BAR("Bar", "bar", { it * 100000.0 }, { it / 100000.0 }),
    PSI("PSI", "psi", { it * 6894.76 }, { it / 6894.76 }),
    ATM("Atmosphere", "atm", { it * 101325.0 }, { it / 101325.0 }),
    MMHG("mmHg", "mmHg", { it * 133.322 }, { it / 133.322 }),
    TORR("Torr", "Torr", { it * 133.322 }, { it / 133.322 }),

    // ===== ENERGY (base: Joule) =====
    JOULE("Joule", "J", { it }, { it }),
    KILOJOULE("Kilojoule", "kJ", { it * 1000.0 }, { it / 1000.0 }),
    CALORIE("Calorie", "cal", { it * 4.184 }, { it / 4.184 }),
    KILOCALORIE("Kilocalorie", "kcal", { it * 4184.0 }, { it / 4184.0 }),
    WATT_HOUR("Watt Hour", "Wh", { it * 3600.0 }, { it / 3600.0 }),
    KILOWATT_HOUR("Kilowatt Hour", "kWh", { it * 3.6e6 }, { it / 3.6e6 }),
    BTU("BTU", "BTU", { it * 1055.06 }, { it / 1055.06 }),
    ELECTRONVOLT("Electronvolt", "eV", { it * 1.602e-19 }, { it / 1.602e-19 }),

    // ===== POWER (base: Watt) =====
    WATT("Watt", "W", { it }, { it }),
    KILOWATT("Kilowatt", "kW", { it * 1000.0 }, { it / 1000.0 }),
    MEGAWATT("Megawatt", "MW", { it * 1e6 }, { it / 1e6 }),
    HORSEPOWER("Horsepower", "hp", { it * 745.7 }, { it / 745.7 }),
    BTU_PER_HOUR("BTU/Hour", "BTU/h", { it * 0.293071 }, { it / 0.293071 }),
    FOOT_POUND_PER_SEC("Foot-Pound/Sec", "ft·lbf/s", { it * 1.35582 }, { it / 1.35582 });

    companion object {
        fun convert(value: Double, from: UnitType, to: UnitType): Double {
            if (from == to) return value
            val baseValue = from.toBase(value)
            return to.fromBase(baseValue)
        }
    }
}
