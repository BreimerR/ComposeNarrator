import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(compose.material)
                implementation(compose.desktop.currentOs)
                implementation(project(":narrator:core-jvm"))
                implementation(project(":samples:lifecycle-aware:common"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "libetal.narrator.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "desktop"
            packageVersion = "1.0.0"
        }
    }
}