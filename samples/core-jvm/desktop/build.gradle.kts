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
                implementation(compose.runtime)
                implementation(compose.material)
                implementation(compose.desktop.currentOs)
                implementation(project(":narrator:core-jvm"))
                implementation(project(":samples:core-jvm:common"))
            }
        }
    }
}


compose.desktop {
    application {
        mainClass = "libetal.kotlin.compose.narrator.desktop.MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "ITrack"
            packageVersion = "1.0.0"
        }
    }
}
