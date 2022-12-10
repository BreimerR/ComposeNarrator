import org.jetbrains.compose.compose

val projectGroup: String by project
val projectVersion: String by project
val androidMinSdkVersion: String by project
val androidTargetSdkVersion: String by project
val androidCompileSdkVersion: String by project

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    `maven-publish`
}

group = projectGroup
version = projectVersion

kotlin {

    android {
        publishLibraryVariants("release", "debug")
    }

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
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
        // binaries.executable()
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)

                api("libetal.libraries.kotlin:io:1.0.2")
                implementation("libetal.libraries.kotlin:log:1.1.0")
                api("libetal.libraries.kotlin:library:1.0.2")
                api("libetal.libraries.kotlin:coroutines:1.0.2")

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

            }
        }

        val commonTest by getting {
            dependencies {

            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.preview)
                compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
            }
        }
        val desktopTest by getting {
            dependencies {
                // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
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