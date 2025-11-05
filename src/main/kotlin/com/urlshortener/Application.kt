package com.urlshortener

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main entry point for the Spring Boot application.
 * Loads environment variables for MongoDB credentials and starts the application.
 */
@SpringBootApplication
class Application

fun main(args: Array<String>) {

    // Load environment variables from a .env file
    val dotenv = dotenv()
    // Set MongoDB credentials as system properties so Spring can use them
    System.setProperty("MONGO_USER", dotenv["MONGO_USER"])
    System.setProperty("MONGO_PASSWORD", dotenv["MONGO_PASSWORD"])

    // Launch the Spring Boot application
    runApplication<Application>(*args)

}