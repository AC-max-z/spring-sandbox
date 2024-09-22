val allureVersion = "2.24.0"
// Define configuration for AspectJ agent
val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok")
    id("org.gradle.test-retry") version "1.5.8"
    id("com.google.cloud.tools.jib") version "3.4.0"
    application
}
group = "com.maxzamota"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

application {
    mainClass = "com.maxzamota.spring_sandbox.SpringBootExampleApplication"
    applicationDefaultJvmArgs = listOf("-XX:+UseZGC")
}


repositories {
    mavenCentral()
}

allprojects {
    version = "${System.getProperty("dockerImageTag") ?: version}"
}

dependencies {
    // faker
    implementation(libs.faker)
    // mapper
    implementation(libs.modelMapper)
    implementation("org.modelmapper:modelmapper-module-record:1.0.0")
    // logging
    implementation(libs.logbackJson)
    implementation(libs.logbackJackson)
    implementation(libs.jacksonDatabind)
    implementation ("com.github.skjolber.logback-logstash-syntax-highlighting-decorators:logback-logstash-syntax-highlighting-decorators:1.0.6")
    agent("org.aspectj:aspectjweaver:1.9.22.1")
    runtimeOnly("org.aspectj:aspectjweaver:1.9.22.1")
    runtimeOnly("org.aspectj:aspectjrt:1.9.22.1")
    // spring-boot-starter
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.hateoas:spring-hateoas")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework:spring-aspects:6.1.12")
    // webflux
    implementation("org.springframework:spring-webflux:6.1.2")
    implementation("org.springframework.boot:spring-boot-configuration-processor:3.3.3")
    // flyway (db migration)
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    // http client
    implementation("io.projectreactor.netty:reactor-netty-http:1.1.14")
    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    //
    runtimeOnly("org.postgresql:postgresql")
    // lombOK
    compileOnly("org.projectlombok:lombok:1.18.+")
    // junit
    testImplementation(libs.junit)
    // test containers
    testImplementation(libs.testContainers)
    testImplementation(libs.testContainersJunit)
    testImplementation(libs.testContainersPostgres)
    // allure
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-junit5")
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
        useJUnitPlatform {
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
        useJUnitPlatform {
            includeTags("unit-test")
        }
    }

    register("integration-tests", Test::class.java) {
        useJUnitPlatform {
            includeTags("integration-test")
        }
    }

    register("service-layer-tests", Test::class.java) {
        useJUnitPlatform {
            includeTags("service-layer")
        }
    }

    register("persistence-layer-tests", Test::class.java) {
        useJUnitPlatform {
            includeTags("persistence-layer")
        }
    }

    register("jpa-tests", Test::class.java) {
        useJUnitPlatform {
            includeTags("jpa")
        }
    }
}

jib {
    from {
        image = "eclipse-temurin:21-alpine"
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