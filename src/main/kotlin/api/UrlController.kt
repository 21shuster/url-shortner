package api

import api.dto.UrlRequestDto
import api.dto.UrlResponseDto
import application.UrlService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

/**
 * REST controller that manages URL shortening operations.
 *
 * Provides endpoints for creating, resolving, updating, deactivating,
 * deleting, and listing shortened URLs.
 */
@RestController
@RequestMapping("/api/url")
class UrlController(private val urlService: UrlService) {

    private val logger = LoggerFactory.getLogger(UrlController::class.java)

    /**
     * Root endpoint of the API.

     * @return A simple message confirming that the API is running.
     */
    @GetMapping("/")
    fun root() = mapOf("message" to "URL Shortener API is running!")

    /**
     * Creates a new shortened URL from a given original URL.
     *
     * @param request The request body containing the original URL, optional description,
     * and optional expiration date.
     * @param clientIp The client's IP address, extracted from the `X-Forwarded-For` header if available.
     * @return A [UrlResponseDto] with the details of the shortened URL.
     */
    @PostMapping("/shorten")
    fun shorten(
        @RequestBody request: UrlRequestDto,
        @RequestHeader(value = "X-Forwarded-For", required = false) clientIp: String?
    ): UrlResponseDto {
        logger.info("Received request to shorten URL: ${request.originalUrl}")
        val url = urlService.shortenUrl(request.originalUrl, request.description, clientIp)
        return UrlResponseDto.fromDomain(url)
    }

    /**
     * Resolves a short code to its original URL.
     *
     * @param code The short code assigned to the shortened URL.
     * @return A map containing the original URL, or an error message if not found or expired.
     */
    @GetMapping("/{code}")
    fun resolve(@PathVariable code: String): Map<String, Any> {
        val original = urlService.getOriginalUrl(code)
        return if (original != null) {
            urlService.incrementClicks(code)
            mapOf("originalUrl" to original)
        } else mapOf("error" to "URL not found or expired")
    }

    /**
     * Updates an existing shortened URL, allowing modification of the
     * original URL or its description.
     *
     * @param code The short code of the URL to update.
     * @param request The updated URL and optional description.
     * @return An updated [UrlResponseDto] or an error message if not found.
     */
    @PutMapping("/{code}/update")
    fun update(
        @PathVariable code: String,
        @RequestBody request: UrlRequestDto
    ): Any {
        val updated = urlService.updateUrl(code, request.originalUrl, request.description)
        return updated?.let { UrlResponseDto.fromDomain(it) }
            ?: mapOf("error" to "URL not found")
    }

    /**
     * Deactivates a shortened URL, making it inaccessible.
     *
     * @param code The short code of the URL to deactivate.
     * @return A message indicating whether the operation was successful.
     */
    @PutMapping("/{code}/deactivate")
    fun deactivate(@PathVariable code: String): Map<String, Any> {
        val success = urlService.deactivateUrl(code)
        return if (success) mapOf("message" to "URL deactivated successfully")
        else mapOf("error" to "URL not found")
    }

    /**
     * Permanently deletes a shortened URL from the database.
     *
     * @param code The short code of the URL to delete.
     * @return A message indicating whether the deletion was successful.
     */
    @DeleteMapping("/{code}")
    fun delete(@PathVariable code: String): Map<String, Any> {
        val success = urlService.deleteUrl(code)
        return if (success) mapOf("message" to "URL deleted successfully")
        else mapOf("error" to "URL not found")
    }

    /**
     * Retrieves all shortened URLs stored in the system.
     *
     * @return A list of [UrlResponseDto] representing all URLs.
     */
    @GetMapping("/all")
    fun getAll(): List<UrlResponseDto> =
        urlService.getAllUrls().map { UrlResponseDto.fromDomain(it) }

}
