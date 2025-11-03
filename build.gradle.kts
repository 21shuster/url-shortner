plugins {
    kotlin("jvm") version "2.2.20"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.spring") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {

    testImplementation(kotlin("test"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Library added for DB vars
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}