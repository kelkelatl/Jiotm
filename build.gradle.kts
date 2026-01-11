// build.gradle.kts (Project Level) - FIXED

plugins {
    // Replaced alias(libs.plugins.android.application)
    id("com.android.application") version "8.2.0" apply false
    
    // Replaced alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}
