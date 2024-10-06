rootProject.name = "spring-sandbox"
pluginManagement {
    val lombokPluginVersion: String by settings
    plugins {
        id("io.freefair.lombok") version lombokPluginVersion
    }
}
include("backend")