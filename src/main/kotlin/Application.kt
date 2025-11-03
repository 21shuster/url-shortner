package org.example

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val dotenv = dotenv()
    System.setProperty("MONGO_USER", dotenv["MONGO_USER"])
    System.setProperty("MONGO_PASSWORD", dotenv["MONGO_PASSWORD"])
    runApplication<Application>(*args)
}