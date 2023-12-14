rootProject.name = "spring-boot-example"
pluginManagement {
    val lombokPluginVersion: String by settings
    plugins {
        id("io.freefair.lombok") version lombokPluginVersion
    }
}
