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
        binaries.executable()
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api("libetal.libraries.kotlin:io:1.0.2")
                /**
                 * Including narrator causes project to fetch
                 * log which is failing for other projects
                 **/
                implementation("libetal.libraries.kotlin:log:1.0.2")
                implementation("libetal.libraries.kotlin:library:1.0.2")
                implementation("libetal.libraries.kotlin:coroutines:1.0.2")

                api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                api(compose.preview)
                // api("androidx.core:core-ktx:1.8.0")
                // api("androidx.appcompat:appcompat:1.5.0")
            }
        }

        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
            }
        }
        val desktopTest by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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