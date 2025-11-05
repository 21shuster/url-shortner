package com.urlshortener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main entry point for the Spring Boot application.
 * Loads environment variables for MongoDB credentials and starts the application.
 */
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    // Launch the Spring Boot application
    runApplication<Application>(*args)
}