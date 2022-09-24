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
                implementation(project(":samples:core-jvm:common"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "libetal.kotlin.compose.narrator.examples.MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "ITrack"
            packageVersion = "1.0.0"
        }
    }
}
