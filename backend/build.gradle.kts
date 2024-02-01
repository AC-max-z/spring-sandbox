val allureVersion = "2.24.0"
// Define configuration for AspectJ agent
val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok")
    id("org.gradle.test-retry") version "1.5.8"
    id("com.google.cloud.tools.jib") version "3.4.0"
}

group = "com.maxzamota"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

allprojects {
    version = "${System.getProperty("dockerImageTag") ?: version}"
}

dependencies {
    implementation(libs.faker)
    implementation(libs.modelMapper)
    implementation(libs.logbackJson)
    implementation(libs.logbackJackson)
    implementation(libs.jacksonDatabind)
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.modelmapper:modelmapper-module-record:1.0.0")
    implementation("org.flywaydb:flyway-core")
    implementation("io.projectreactor.netty:reactor-netty-http:1.1.14")
    // https://mvnrepository.com/artifact/org.springframework/spring-webflux
    implementation("org.springframework:spring-webflux:6.1.2")

    runtimeOnly("org.postgresql:postgresql")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok:1.18.+")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.junit)
    testImplementation(libs.testContainers)
    testImplementation(libs.testContainersJunit)
    testImplementation(libs.testContainersPostgres)
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-junit5")
    agent("org.aspectj:aspectjweaver:1.9.20.1")
}

tasks {
    withType<Test> {
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
            excludeTags("E2E")
        }
        retry {
            maxRetries.set(3)
            maxFailures.set(20)
            failOnPassedAfterRetry.set(false)
        }
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 4).takeIf { it > 0 } ?: 1
    }

    register("unit-tests", Test::class.java) {
        useJUnitPlatform() {
            includeTags("unit-test")
        }
    }

    register("integration-tests", Test::class.java) {
        useJUnitPlatform() {
            includeTags("integration-test")
        }
    }

    register("service-layer-tests", Test::class.java) {
        useJUnitPlatform() {
            includeTags("service-layer")
        }
    }

    register("persistence-layer-tests", Test::class.java) {
        useJUnitPlatform() {
            includeTags("persistence-layer")
        }
    }

    register("jpa-tests", Test::class.java) {
        useJUnitPlatform() {
            includeTags("jpa")
        }
    }
}

jib {
    from {
        image = "eclipse-temurin:21"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "docker.io/acidcommunism69/spring-sandbox-api:${version}"
        setTags(listOf("latest", "$version"))
    }
}