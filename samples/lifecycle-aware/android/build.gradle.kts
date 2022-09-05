@file:Suppress("UnstableApiUsage")

val projectGroup: String by project
val projectVersion: String by project
val androidMinSdkVersion: String by project
val androidTargetSdkVersion: String by project
val androidCompileSdkVersion: String by project

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "$projectGroup.narrator.samples.android"
version = projectVersion

dependencies {
    implementation(project(":samples:lifecycle-aware:common"))
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("com.google.android.material:material:1.6.1")
}

android {
    compileSdk = androidCompileSdkVersion.toInt()
    defaultConfig {
        applicationId = "$projectGroup.narrator.lifecycle.aware.android"
        minSdk = androidMinSdkVersion.toInt()
        targetSdk = androidTargetSdkVersion.toInt()
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        excludes += "META-INF/log.kotlin_module"
    }

    buildTypes {

        getByName("debug") {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}