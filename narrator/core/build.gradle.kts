@file:Suppress("UnstableApiUsage", "DEPRECATION")

val projectGroup: String by project
val composeVersion: String by project
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

val javaTargetVersion: String by extra
val javaVersion: JavaVersion by extra

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.library)
    `maven-publish`
}

group = "$projectGroup.narrator"
version = projectVersion

kotlin {

    androidTarget()

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = javaTargetVersion
        }
    }

    js(IR) {
        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(libs.libetal.logs)
                api(libs.libetal.kotlin)
                api(libs.jetbrains.datetime)
                api(libs.jetbrains.kotlinx.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                // implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(compose.runtime)
                implementation(compose.foundation)
            }
        }


        val desktopMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(compose.runtime)
                implementation(compose.foundation)
                api(libs.jetbrains.kotlinx.coroutines.swing)
            }
        }

        val desktopTest by getting {
            dependencies {
                api(libs.jetbrains.kotlinx.coroutines.swing)
            }
        }

        val jsMain by getting {
            dependencies {

            }
        }

    }
}

android {
    namespace = "libetal.kotlin.compose.narrator.core"
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

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }

    buildFeatures {
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


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        kotlin.sourceSets.all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}
