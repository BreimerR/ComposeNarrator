@file:Suppress("UnstableApiUsage")

import org.jetbrains.compose.compose

val projectGroup: String by project
val projectVersion: String by project
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
    id("org.jetbrains.compose")
    id("com.android.library")
    `maven-publish`
}

group = "$projectGroup.narrator"
version = projectVersion

kotlin {

    android {
        publishLibraryVariants("release", "debug")
        compilations.all {
            kotlinOptions.jvmTarget = jvmTargetVersion
        }
    }

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = jvmTargetVersion
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.runtime)
                api(compose.foundation)
                api(project(":narrator:core"))
                api("libetal.libraries.kotlin:log:$libetalKotlinLogVersion")
                api("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDateTime")
                api("libetal.libraries.kotlin:library:$libetalKotlinVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutines")
            }
        }

        val commonTest by getting {
            dependencies {
                api(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                api("androidx.core:core-ktx:$androidCoreKtx")
                api("androidx.appcompat:appcompat:$androidAppCompat")
            }
        }

       /* val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }

        }*/

        val desktopMain by getting {
            dependencies {
                compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$kotlinSwingCoroutines")
            }
        }

        val desktopTest by getting {
            dependencies {
                compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$kotlinSwingCoroutines")
            }
        }
    }
}

android {

    compileSdk = androidCompileSdkVersion.toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = androidMinSdkVersion.toInt()
        targetSdk = androidTargetSdkVersion.toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
