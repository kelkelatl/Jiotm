// app/build.gradle.kts (Module Level)

plugins {
    // Apply the necessary plugins for an Android application using Kotlin
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    // Configuration for the build environment
    namespace = "dev.kelvinwilliams.PRE" // Your specified package name
    compileSdk = 34 // Example target compilation SDK

    defaultConfig {
        applicationId = "dev.kelvinwilliams.PRE"
        minSdk = 24 // Minimum supported Android version
        targetSdk = 34 // Target Android version
        versionCode = 1
        versionName = "1.0"

        // Test configuration (minimal)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Configuration for release and debug builds
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    // Enable Java 17 compatibility for modern Kotlin features
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

// Dependency block: Add required libraries
dependencies {

    // Standard AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    
    // Dependencies for widget UI (though minimal for this project)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout) 
    
    // Testing dependencies (minimal)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
