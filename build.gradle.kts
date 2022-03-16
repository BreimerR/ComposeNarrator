buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    val composeVersion: String by project
    val kotlinVersion: String by project

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        @Suppress("AndroidGradlePluginVersion")
        classpath("com.android.tools.build:gradle:7.0.2")
    }
}

group = "libetal.kotlin.compose.narrator"
version = "1.0.0"

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}