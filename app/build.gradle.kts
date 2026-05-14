plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.santheconnect.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.santheconnect.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Add your Google Maps API key in local.properties as:
        // MAPS_API_KEY=your_key_here
        manifestPlaceholders["MAPS_API_KEY"] = ""
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.icons.extended)
    implementation(libs.material)

    // Coil for image loading
    implementation(libs.coil.compose)

    // Google Maps
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Firebase Firestore (uncomment after adding google-services.json)
    // implementation(platform(libs.firebase.bom))
//    // implementation(libs.firebase.firestore)
//    implementation("com.google.firebase:firebase-auth:22.3.0")
//    implementation("com.google.firebase:firebase-firestore:24.10.0")

    debugImplementation(libs.androidx.ui.tooling)

    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.0")

//    implementation("com.google.firebase:firebase-storage-ktx")

    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
}
