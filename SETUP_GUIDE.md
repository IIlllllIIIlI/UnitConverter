# Convertly - Setup & Publishing Guide

## Before You Build

### 1. AdMob Setup (Required for Ad Revenue)
1. Create an AdMob account at https://admob.google.com
2. Create a new app in AdMob and get your **App ID**
3. Create a **Banner Ad Unit** and get the **Ad Unit ID**
4. Replace the placeholders in:
   - `AndroidManifest.xml`: Replace `ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY` with your AdMob App ID
   - `app/src/main/java/com/convertly/app/ads/AdManager.kt`: Replace the test banner ID with your real Ad Unit ID

> **IMPORTANT**: The app currently uses Google's test ad IDs, which will show test ads during development. You MUST replace them with real IDs before publishing.

### 2. Google Play Billing Setup (Required for $0.99 Ad Removal)
1. In Google Play Console, go to your app > Monetize > In-app products
2. Create a new in-app product:
   - **Product ID**: `remove_ads_lifetime`
   - **Name**: Remove Ads
   - **Description**: One-time purchase for lifetime ad-free experience
   - **Price**: $0.99
3. Activate the product

### 3. App Signing
1. Generate a keystore:
   ```bash
   keytool -genkey -v -keystore convertly.jks -keyalias convertly -keyalg RSA -keysize 2048 -validity 10000
   ```
2. Update `app/build.gradle.kts` signing config with your keystore details
3. Or use Google Play App Signing (recommended)

## Building

```bash
# Debug build
./gradlew assembleDebug

# Release build (after configuring signing)
./gradlew assembleRelease

# Generate AAB for Play Store (recommended)
./gradlew bundleRelease
```

## Google Play Store Listing

### Suggested Details:
- **App Name**: Convertly - Unit Converter
- **Short Description**: Simple, fast unit converter with 12 categories and 100+ units
- **Category**: Tools
- **Content Rating**: Everyone

### Feature Highlights for Listing:
- 12 conversion categories (Length, Weight, Temperature, Currency, Volume, Area, Speed, Time, Data, Pressure, Energy, Power)
- 100+ units supported
- Real-time conversion as you type
- Save favorites for quick access
- Conversion history
- 8 accent color themes
- Light/Dark/System theme modes
- Customizable decimal places
- Copy results to clipboard
- Haptic feedback
- Clean Material 3 design
- Ad-free option for $0.99

### Privacy Policy
You need a privacy policy since the app uses:
- Google AdMob (collects device info for ad personalization)
- Google Play Billing (processes purchases)
- No personal data is collected by the app itself
- All conversion history is stored locally on-device

## Revenue Streams
1. **Banner Ads**: Shown at bottom of home and converter screens
2. **Ad Removal**: $0.99 one-time purchase removes all ads permanently

## File Structure
```
app/src/main/java/com/convertly/app/
├── ConvertlyApp.kt          # Application class
├── MainActivity.kt           # Main activity with Compose
├── ads/
│   └── AdManager.kt         # AdMob integration
├── billing/
│   └── BillingManager.kt    # Google Play Billing
├── data/
│   ├── model/
│   │   ├── UnitCategory.kt  # 12 unit categories
│   │   ├── UnitType.kt      # 100+ unit types with conversion logic
│   │   └── ConversionHistory.kt
│   └── repository/
│       └── PreferencesRepository.kt  # DataStore preferences
└── ui/
    ├── components/
    │   ├── AdBanner.kt       # Ad banner composable
    │   └── UnitDropdown.kt   # Unit selector dropdown
    ├── navigation/
    │   └── NavGraph.kt       # Navigation routes
    ├── screens/
    │   ├── HomeScreen.kt     # Category grid
    │   ├── ConverterScreen.kt # Main converter
    │   ├── SettingsScreen.kt # App settings
    │   ├── HistoryScreen.kt  # Conversion history
    │   ├── FavoritesScreen.kt # Saved favorites
    │   └── MainViewModel.kt  # Shared ViewModel
    └── theme/
        ├── Color.kt          # Color definitions
        └── Theme.kt          # Material 3 theming
```
