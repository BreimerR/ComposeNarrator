val projectGroup: String by project
val projectVersion: String by project
val jvmTargetVersion: String by project

val androidMinSdkVersion: String by project
val androidTargetSdkVersion: String by project
val androidCompileSdkVersion: String by project

plugins {
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

group = "$projectGroup.narrator.samples.common"
version = projectVersion


kotlin {

    android {
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
                api(compose.material)
                api(compose.animation)
                api(compose.materialIconsExtended)
                implementation("libetal.libraries.kotlin:log:1.1.0")
                api(project(":narrator:lifecycle-aware"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }

}

@Suppress("UnstableApiUsage")
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