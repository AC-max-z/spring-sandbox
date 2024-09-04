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
    // junit
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    // selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.16.1")
    // assertj
    testImplementation("org.assertj:assertj-core:3.25.1")
    // faker
    testImplementation(libs.faker)
    // logging
    implementation("ch.qos.logback:logback-core:1.5.7")
    implementation("org.slf4j:slf4j-api:2.0.11")
    // duct tape
    implementation("org.rnorth.duct-tape:duct-tape:1.0.8")
    // allure
    testImplementation(platform("io.qameta.allure:allure-bom:2.24.0"))
    testImplementation("io.qameta.allure:allure-junit5")
    // lombok
    compileOnly("org.projectlombok:lombok:1.18.+")
    // jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.2")
}

tasks.test {
    useJUnitPlatform() {
        excludeTags("E2E", "UI")
    }
    systemProperties["junit.jupiter.execution.parallel.enabled"] = true
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 4).takeIf { it > 0 } ?: 1
    retry {
        maxRetries.set(2)
        failOnPassedAfterRetry.set(false)
    }
}
tasks {
    register("e2e-ui-tests", Test::class.java) {
        testLogging {
            showExceptions = true
            showCauses = true
            events("passed")
        }
        val includeTags = System.getProperty("includeTags")
        val excludeTags = System.getProperty("excludeTags")
        useJUnitPlatform() {
            if (!includeTags.isNullOrEmpty()) {
                includeTags(includeTags)
            }
            if (!excludeTags.isNullOrEmpty()) {
                excludeTags(excludeTags)
            }
            includeTags("E2E", "UI")
        }
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 4).takeIf { it > 0 } ?: 1
        retry {
            maxRetries.set(2)
            failOnPassedAfterRetry.set(false)
        }
    }
}