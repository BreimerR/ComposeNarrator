pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}
rootProject.name = "ComposeNarrator"


include(":example:android")
include(":example:desktop")
include(":example:common")
include(":narrator")

