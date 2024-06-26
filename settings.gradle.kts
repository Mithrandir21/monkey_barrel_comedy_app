rootProject.name = "MonkeyBarrelComey"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()

        // Required by the WebView project for Desktop target
        maven("https://jogamp.org/deployment/maven")
    }
}

include(":app")
include(":logging")
include(":remote")
include(":domain")
include(":common")
include(":compose")
include(":testing")
include(":feature:home")
include(":feature:shows")
include(":feature:news")
include(":feature:webview")
include(":feature:blogs")
include(":feature:artists")
include(":feature:podcasts")
include(":feature:merch")
