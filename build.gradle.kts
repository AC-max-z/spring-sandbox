val allureVersion = "2.24.0"
val fakerVersion = "0.13"
val modelMapperVersion = "2.3.8"
val junitVersion = "5.8.1"
val testContainersVersion = "1.19.3"

plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok")
    id("org.gradle.test-retry") version "1.5.8"
}

group = "com.maxzamota"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.javafaker:javafaker:$fakerVersion")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.modelmapper:modelmapper:$modelMapperVersion")
    implementation("org.modelmapper:modelmapper-module-record:1.0.0")
    implementation("org.flywaydb:flyway-core")
    implementation("io.projectreactor.netty:reactor-netty-http:1.1.14")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    // https://mvnrepository.com/artifact/org.springframework/spring-webflux
    implementation("org.springframework:spring-webflux:6.1.2")
    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok:1.18.+")
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-junit5")
}

tasks {
    withType<Test> {
        testLogging {
            showExceptions = true
            showCauses = true
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
        }
        retry {
            maxRetries.set(3)
            maxFailures.set(20)
            failOnPassedAfterRetry.set(true)
        }
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
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
