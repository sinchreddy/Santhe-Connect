# 📱 Santhe-Connect

## 🧭 Overview
Santhe-Connect is an Android application designed to digitally showcase and promote **local Karnataka culture**, including weekly markets (Santhe), traditional food spots, and homestays.  

The application aims to bridge the gap between **local vendors and tourists** by improving digital visibility for small-scale businesses that are often not listed on mainstream platforms.

---

## 🎯 Objective
To develop a mobile platform that enables users to:
- Discover authentic local experiences
- Explore weekly market schedules
- Access nearby food and homestay options
- Connect with local vendors in a structured digital ecosystem

---

## 🧩 Problem Statement
Tourists and users face difficulty in finding genuine local cultural experiences such as weekly markets, traditional food places, and homestays due to lack of centralized digital availability.  

Santhe-Connect addresses this issue by providing a **location-aware mobile platform** that connects users directly with local culture and services.

---

## ✨ Key Features
- 📍 Location-based discovery of Santhe (weekly markets)
- 🍛 Listings of authentic local food outlets
- 🏡 Homestay exploration module
- 🔍 Smart search and filtering system
- ⭐ Ratings and user review system
- 📅 Weekly market schedule viewing
- 📊 Structured vendor and place information display

---

## 🛠️ Tech Stack
- **Language:** Kotlin   
- **Framework:** Android SDK / Jetpack Compose 
- **Backend:** Firebase 
- **IDE:** Android Studio  
- **Version Control:** Git & GitHub  

---

## ⚙️ Installation Guide

### 💻 Windows / Linux Setup
```bash
git clone https://github.com/your-username/santhe-connect.git
```
1. Open Android Studio
2. Click on Open Project
3. Select the cloned repository folder
4. Allow Gradle Sync to complete
5. Install missing dependencies if prompted
6. Connect an emulator or Android device
7. Click Run ▶️

### 💻 MacOS Setup
```bash
git clone https://github.com/your-username/santhe-connect.git
```
1. Open Android Studio (macOS)
2. Click Open from the welcome screen
3. Select the cloned project directory
4. Wait for Gradle Sync and indexing
5. Install required SDK components if prompted
6. Configure emulator via AVD Manager or connect a physical device
7. Click Run ▶️

---

## ▶️ Build & Run

You can run the project using:
```bash
./gradlew assembleDebug
```
Or directly using Android Studio:
* Click Run ▶️

---

## 🌐 Project Links

* 🔗 GitHub Repository: https://github.com/sinchreddy/santhe-connect
* 📦 APK / Release: https://drive.google.com/drive/folders/1AfhnYbWz2r3QKGYcIljiUwTZopY677pv

---

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

---

## Setup in Android Studio

1. **Open Android Studio** (Hedgehog 2023.1.1 or newer recommended)
2. **File → New → New Project** → choose "Empty Activity" with Kotlin + Compose
3. Set **Package name**: `com.santheconnect`
4. After project is created, **replace** the generated files with the source files provided
5. **Sync Gradle** (File → Sync Project with Gradle Files)
6. **Run** on an emulator or physical device (API 26+)

---

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- Kotlin 1.9+
- Compose BOM 2024.x
- minSdk 26 (Android 8.0+)
- compileSdk / targetSdk 35

---

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

---

## Screens

| Tab | File | Description |
|-----|------|-------------|
| 🏠 Home | `HomeScreen.kt` | Hero banner, category grid, today's santhes, reviews |
| 🗺️ Santhe Map | `SantheMapReal.kt` | Calendar with day & type filters, map placeholder |
| 🍽️ Eat Local | `EatLocalScreen.kt` | Today's special banner, khana-vali listings with ratings |
| Review| `ReviewScreen.kt` | Add reviews for all places through audio, images and also can add location |
| ➕ Add | `AddLocationScreen.kt` | Form to submit new locations with GPS capture |

---
## 📊 Impact

* Improves visibility of local vendors and small businesses
* Encourages cultural tourism in Karnataka
* Provides structured digital access to rural and local markets
* Bridges gap between offline markets and digital platforms

---

## Future Enhancements

* 🔐 Firebase Authentication (User Login/Signup)
* 📍 Real-time GPS-based recommendations
* 🤖 AI-powered personalized suggestions
* 💬 Chat system between users and vendors
* 💳 Online booking/payment integration
* 🌐 Multi-language support (Kannada, English)

---

## 👩‍💻 Developer

Sinchana N
