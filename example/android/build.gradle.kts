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
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "libetal.kotlin.compose.narrator.android"
        minSdkVersion(24)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}