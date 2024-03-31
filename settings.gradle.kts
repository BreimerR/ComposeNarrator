rootProject.name = "Narrator"

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
dependencyResolutionManagement {
    ///repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":narrator:core")
include(":narrator:core-jvm")
include(":narrator:lifecycle")
include(":narrator:lifecycle-aware")

include(":samples:core-jvm:common")
include(":samples:core-jvm:android")
include(":samples:core-jvm:desktop")

include(":samples:lifecycle-aware:common")
include(":samples:lifecycle-aware:android")
include(":samples:lifecycle-aware:desktop")
