plugins {
    id("java")
    id("org.gradle.test-retry") version "1.5.8"
}

group = "org.springsandbox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.seleniumhq.selenium:selenium-java:4.16.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.assertj:assertj-core:3.25.1")
    testImplementation(libs.faker)
    implementation("org.rnorth.duct-tape:duct-tape:1.0.8")
    implementation("org.slf4j:slf4j-api:2.0.11")
    testImplementation(platform("io.qameta.allure:allure-bom:2.24.0"))
    testImplementation("io.qameta.allure:allure-junit5")
}

tasks.test {
    useJUnitPlatform()
    systemProperties["junit.jupiter.execution.parallel.enabled"] = true
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 4).takeIf { it > 0 } ?: 1
    retry {
        maxRetries.set(2)
        failOnPassedAfterRetry.set(false)
    }
}