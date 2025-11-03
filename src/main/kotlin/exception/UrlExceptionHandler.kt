package org.example.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Exception handler for the URL Shortener API.
 * Catches specific exceptions and returns meaningful HTTP responses.
 */
@ControllerAdvice
class UrlApiExceptionHandler {

    /**
     * Handles invalid URL format.
     * Returns 400 Bad Request with an error message.
     */
    @ExceptionHandler(InvalidUrlException::class)
    fun handleInvalidUrl(ex: InvalidUrlException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to ex.message.orEmpty()))
    }

    /**
     * Handles URL not found errors.
     * Returns 404 Not Found.
     */
    @ExceptionHandler(UrlNotFoundException::class)
    fun handleUrlNotFound(ex: UrlNotFoundException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to ex.message.orEmpty()))
    }

    /**
     * Handles general runtime exceptions.
     * Returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("error" to "Unexpected error: ${ex.localizedMessage}"))
    }
}

/**
 * Custom exception for invalid URLs.
 */
class InvalidUrlException(message: String) : RuntimeException(message)

/**
 * Custom exception when URL is not found.
 */
class UrlNotFoundException(message: String) : RuntimeException(message)
