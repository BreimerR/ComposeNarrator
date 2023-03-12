@file:Suppress("UnstableApiUsage")

import org.jetbrains.compose.compose

val projectGroup: String by project
val projectVersion: String by project
val jvmTargetVersion: String by project
val androidMinSdkVersion: String by project
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
                implementation(compose.runtime)

                implementation("libetal.libraries.kotlin:log:1.1.0")
                implementation("libetal.libraries.kotlin:library:1.0.3")
                implementation("libetal.libraries.kotlin:coroutines:1.0.3")

                api(project(":narrator:core"))
                api(project(":narrator:lifecycle"))

            }
        }

        val commonTest by getting {
            dependencies {

            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.animation)
                api(project(":narrator:core-jvm"))
            }
        }

        val androidTest by getting {
            dependencies {
                //  implementation("junit:junit:4.13.2")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.animation)
                api(project(":narrator:core-jvm"))
            }
        }

        val desktopTest by getting {
            dependencies {
            }
        }

        val jsMain by getting {
            dependencies {

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


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        kotlin.sourceSets.all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}