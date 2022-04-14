val projectVersion: String by project
val packageName: String by project

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "$packageName.narrator.samples.android"
version = projectVersion

repositories {

}

dependencies {
    implementation(project(":example:common"))
    implementation("androidx.activity:activity-compose:1.4.0")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "libetal.kotlin.compose.narrator.android"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions{
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