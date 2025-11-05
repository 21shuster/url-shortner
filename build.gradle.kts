plugins {
    kotlin("jvm") version "2.2.20"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.spring") version "2.0.20"
}

group = "com.urlshortener"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {

    testImplementation(kotlin("test"))
    // Spring Boot starter for building REST APIs
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Data MongoDB (NoSQL database)
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Kotlin support for reflection & standard library
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // --- Testing frameworks ---
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito", module = "mockito-core")
    }

    // --- Mockito for Kotlin ---
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")

    // --- Assertions ---
    testImplementation("org.assertj:assertj-core:3.26.0")

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}