// settings.gradle.kts (Project Root)

pluginManagement {
    repositories {
        // This is where the Android and Kotlin plugins live!
        google() 
        mavenCentral()
        gradlePluginPortal()
    }
}
// Include the app module
include(":app") 

// Older projects sometimes rely on a top-level build.gradle.kts repositories 
// block as a fallback, so let's update that one too just in case.
