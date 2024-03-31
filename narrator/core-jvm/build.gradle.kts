@file:Suppress("UnstableApiUsage")

import org.jetbrains.compose.compose

val projectGroup: String by project
val projectVersion: String by project
val composeVersion: String by project
val kotlinDateTime: String by project
val androidCoreKtx: String by project
val androidAppCompat: String by project
val jvmTargetVersion: String by project
val kotlinCoroutines: String by project
val androidMinSdkVersion: String by project
val libetalKotlinVersion: String by project
val kotlinSwingCoroutines: String by project
val libetalKotlinLogVersion: String by project
val androidTargetSdkVersion: String by project
val androidCompileSdkVersion: String by project

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.library)
    `maven-publish`
}

val javaVersion: JavaVersion by extra
val javaTargetVersion: String by extra

group = "$projectGroup.narrator"
version = projectVersion

kotlin {
    androidTarget()

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = javaTargetVersion
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.runtime)
                api(compose.foundation)
                api(project(":narrator:core"))
                api(libs.libetal.logs)
                api(libs.libetal.kotlin)
                api(libs.jetbrains.datetime)
                api(libs.jetbrains.kotlinx.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                api(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {

            }
        }

       /* val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }

        }*/

        val desktopMain by getting {
            dependencies {
                api(libs.jetbrains.kotlinx.coroutines.swing)
            }
        }

        val desktopTest by getting {
            dependencies {
                api(libs.jetbrains.kotlinx.coroutines.swing)
            }
        }
    }
}

android {
    namespace = "libetal.kotlin.compose.narrator.jvm"
    compileSdk = androidCompileSdkVersion.toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = androidMinSdkVersion.toInt()
        targetSdk = androidTargetSdkVersion.toInt()
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    composeOptions{
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }

    buildFeatures{
        compose = true
    }

}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
    }
}


/*
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        kotlin.sourceSets.all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}
*/
