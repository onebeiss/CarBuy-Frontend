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
    versionCatalogs {
        create("libs") {
            // ... otras dependencias que ya tengas
            library("glide", "com.github.bumptech.glide:glide:4.13.0")
            library("glide-compiler", "com.github.bumptech.glide:compiler:4.13.0")
        }
    }
}

rootProject.name = "CarBuy"
include(":app")
