# SantheConnect – Android App (Jetpack Compose)

## Project Structure

```
app/src/main/java/com/santheconnect/
├── MainActivity.kt                         ← Entry point
├── data/
│   └── Models.kt                           ← Data classes + sample data
├── ui/
│   ├── SantheConnectApp.kt                 ← Root composable, Header, TabBar
│   ├── theme/
│   │   └── Theme.kt                        ← Colors, MaterialTheme
│   ├── components/
│   │   └── SantheCard.kt                   ← Reusable SantheCard + Chip
│   └── screens/
│       ├── HomeScreen.kt                   ← Home tab
│       ├── SantheMapScreen.kt              ← Map/Calendar tab
│       ├── EatLocalScreen.kt               ← Eat Local tab
│       └── AddLocationScreen.kt            ← Add Location tab
```

## Setup in Android Studio

1. **Open Android Studio** (Hedgehog 2023.1.1 or newer recommended)
2. **File → New → New Project** → choose "Empty Activity" with Kotlin + Compose
3. Set **Package name**: `com.santheconnect`
4. After project is created, **replace** the generated files with the source files provided
5. **Sync Gradle** (File → Sync Project with Gradle Files)
6. **Run** on an emulator or physical device (API 26+)

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- Kotlin 1.9+
- Compose BOM 2024.x
- minSdk 26 (Android 8.0+)
- compileSdk / targetSdk 35

## Dependencies (already in default Compose project)

All dependencies are standard Jetpack Compose + Material3 — no extra libraries needed:

```kotlin
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.ui)
implementation(libs.androidx.ui.graphics)
implementation(libs.androidx.material3)
implementation(libs.androidx.activity.compose)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.core.ktx)
```

## Screens

| Tab | File | Description |
|-----|------|-------------|
| 🏠 Home | `HomeScreen.kt` | Hero banner, category grid, today's santhes, reviews |
| 🗺️ Santhe Map | `SantheMapScreen.kt` | Calendar with day & type filters, map placeholder |
| 🍽️ Eat Local | `EatLocalScreen.kt` | Today's special banner, khana-vali listings with ratings |
| ➕ Add | `AddLocationScreen.kt` | Form to submit new locations with GPS capture |

## Next Steps / TODO

- [ ] Integrate Google Maps SDK for real map view
- [ ] Add Room database for local storage
- [ ] Implement GPS location capture with `FusedLocationProviderClient`
- [ ] Add Firebase backend for location submissions
- [ ] Implement search functionality
- [ ] Add image upload for locations
