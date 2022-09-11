plugins {
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

kotlin {
    jvm("desktop") {

    }

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":narrator:core-jvm"))
                implementation(project(":samples:lifecycle-aware-jvm:common"))
            }
        }
    }
}