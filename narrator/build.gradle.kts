import org.jetbrains.compose.compose

val projectVersion: String by project
val packageName: String by project

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    `maven-publish`
}

group = packageName
version = projectVersion

kotlin {
    android()

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
                api("libetal.multiplatform:log:1.0.0")
                api("libetal.kotlin:log:1.0.0")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

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
                api("androidx.appcompat:appcompat:1.4.1")
                api("androidx.core:core-ktx:1.7.0")
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
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.1")
            }
        }
        val desktopTest by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.1")
            }
        }
    }
}

android {
    compileSdk = (31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = (24)
        targetSdk = (31)
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