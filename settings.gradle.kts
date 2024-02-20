pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven {
            url = uri("https://atlas.microsoft.com/sdk/android")
        }
    }
}

rootProject.name = "CheckInFace"
include(":app")
 