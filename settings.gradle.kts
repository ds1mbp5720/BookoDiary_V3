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
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BookDiary_V3"
include(":app")
include(":presentation")
include(":domain:book")
include(":domain:history")
include(":domain:offstore")
include(":data:offstore")
include(":data:book")
include(":core:network")
include(":core:database")
include(":domain:shared")
