plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.android.application) apply false
    kotlin("multiplatform") apply false version libs.versions.kotlin
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

val projectGroup: String by project

group = projectGroup
version = "1.0.0"

allprojects {
    extra["javaTargetVersion"] = "11"
    extra["javaVersion"] = JavaVersion.VERSION_11
}
