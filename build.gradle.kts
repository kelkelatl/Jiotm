// build.gradle.kts (Project Level)

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // These plugins are required for a standard Android application
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// Defines where Gradle should look for dependencies (e.g., Google's repository, Maven Central)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}
