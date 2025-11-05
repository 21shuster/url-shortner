package api.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

/**
 * Global exception handler for the URL Shortener API.
 */
@ControllerAdvice
class UrlApiExceptionHandler {

    private val logger = LoggerFactory.getLogger(UrlApiExceptionHandler::class.java)

    data class ErrorResponse(
        val timestamp: Instant = Instant.now(),
        val status: Int,
        val error: String,
        val message: String
    )

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleInvalidUrl(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.warn("Validation error: ${ex.message}")
        val response = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid input"
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unhandled exception", ex)
        val response = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = ex.message ?: "Unexpected error"
        )
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}