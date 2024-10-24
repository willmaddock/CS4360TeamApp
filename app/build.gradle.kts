plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services") // For Firebase services
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) // For managing API keys securely
}

android {
    namespace = "com.example.cs4360app"
    compileSdk = 34 // Updated to the latest version (34)

    defaultConfig {
        applicationId = "com.example.cs4360app"
        minSdk = 25 // Maintain the minimum SDK version
        //noinspection OldTargetApi
        targetSdk = 34 // Updated to the latest version (34)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Include the API key from gradle.properties for Google Maps
        buildConfigField("String", "MAPS_API_KEY", "\"${project.findProperty("MAPS_API_KEY") ?: ""}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // Debug-specific configurations can go here
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true // Enable ViewBinding
        compose = true     // Enable Jetpack Compose
        buildConfig = true // Enable BuildConfig
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Exclude unnecessary resources
        }
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)

    // Firebase dependencies
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.messaging.ktx) // Added FCM dependency

    // Facebook SDK (for social login or sharing)
    implementation(libs.facebook.android.sdk)

    // Google Maps and Places APIs
    implementation(libs.play.services.maps)
    implementation(libs.places.v400)
    implementation(libs.play.services.location)

    // RecyclerView and Activity dependencies
    implementation(libs.androidx.recyclerview) // RecyclerView dependency
    implementation(libs.androidx.activity)

    // Google Sign-In
    implementation(libs.play.services.auth)
    implementation(libs.generativeai)

    // Retrofit for networking and Gson for JSON parsing
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Apply the Google services Gradle plugin
apply(plugin = "com.google.gms.google-services")