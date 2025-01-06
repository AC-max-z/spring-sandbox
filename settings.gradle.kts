rootProject.name = "spring-sandbox"
pluginManagement {
    val lombokPluginVersion: String by settings
    plugins {
        id("io.freefair.lombok") version lombokPluginVersion
    }
    pluginManagement {
        repositories {
            maven { url = uri("https://repo.spring.io/milestone") }
            maven { url = uri("https://repo.spring.io/snapshot") }
            gradlePluginPortal()
        }
    }
}
include("backend")