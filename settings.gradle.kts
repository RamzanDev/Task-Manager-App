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

rootProject.name = "TaskManagerApp"
include(":app")
include(":core")
include(":feature-auth")
include(":data")
include(":domain")
include(":mvi-base")
include(":core-ui")
include(":ui-kit")
include(":locale")
include(":feature-main")
