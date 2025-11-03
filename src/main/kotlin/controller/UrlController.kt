package org.example.controller

import org.example.model.Url
import org.example.service.UrlService
import org.springframework.web.bind.annotation.*

/**
 * REST controller exposing endpoints for URL shortening and management.
 */
@RestController
@RequestMapping("/api/url")
class UrlController(private val urlService: UrlService) {

    /**
     * GET endpoint for root path.
     * Simple health check / welcome message.
     */
    @GetMapping("/")
    fun root(): Map<String, String> {
        return mapOf("message" to "URL Shortener API is running!")
    }

    /**
     * POST endpoint to shorten a given URL.
     * Accepts parameters:
     * - url: the original URL to shorten
     * - description: optional description of the URL
     * - clientIp: optional client IP (from headers)
     * Returns a map with the short code and URL details.
     */
    @PostMapping("/shorten")
    fun shorten(
        @RequestParam url: String,
        @RequestParam(required = false) description: String?,
        @RequestHeader(value = "X-Forwarded-For", required = false) clientIp: String?
    ): Map<String, Any?> {
        val savedUrl: Url = urlService.shortenUrl(url, description, clientIp)
        return mapOf(
            "shortCode" to savedUrl.shortCode,
            "originalUrl" to savedUrl.originalUrl,
            "createdAt" to savedUrl.createdAt,  // Instant timestamp of creation
            "description" to savedUrl.description,
            "clicks" to savedUrl.clicks,        // Number of times URL has been accessed
            "active" to savedUrl.active         // Status if URL is active
        )
    }

    /**
     * GET endpoint to resolve the original URL from a short code.
     * Increments the click count if URL exists.
     * Returns the original URL or an error message if not found.
     */
    @GetMapping("/{code}")
    fun resolve(@PathVariable code: String): Map<String, Any?> {
        val original = urlService.getOriginalUrl(code)
        return if (original != null) {
            urlService.incrementClicks(code)
            mapOf("originalUrl" to original)
        } else {
            mapOf("error" to "URL not found or expired")
        }
    }

    /**
     * DELETE endpoint to remove a URL by its short code.
     * Returns a success or error message.
     */
    @DeleteMapping("/{code}")
    fun delete(@PathVariable code: String): Map<String, Any> {
        val success = urlService.deleteUrl(code)
        return if (success) {
            mapOf("message" to "URL deleted successfully")
        } else {
            mapOf("error" to "URL not found")
        }
    }

    /**
     * PUT endpoint to update a URL or its description.
     * Accepts optional new URL and new description.
     * Returns updated URL details or an error message.
     */
    @PutMapping("/{code}/update")
    fun update(
        @PathVariable code: String,
        @RequestParam(required = false) newUrl: String?,
        @RequestParam(required = false) newDescription: String?
    ): Map<String, Any?> {
        val updatedUrl = urlService.updateUrl(code, newUrl, newDescription)
        return if (updatedUrl != null) {
            mapOf(
                "shortCode" to updatedUrl.shortCode,
                "originalUrl" to updatedUrl.originalUrl,
                "description" to updatedUrl.description,
                "createdAt" to updatedUrl.createdAt,
                "clicks" to updatedUrl.clicks,
                "active" to updatedUrl.active
            )
        } else {
            mapOf("error" to "URL not found")
        }
    }

    /**
     * PUT endpoint to deactivate a short URL manually.
     * Returns success or error message.
     */
    @PutMapping("/{code}/deactivate")
    fun deactivate(@PathVariable code: String): Map<String, Any> {
        val success = urlService.deactivateUrl(code)
        return if (success) {
            mapOf("message" to "URL deactivated successfully")
        } else {
            mapOf("error" to "URL not found")
        }
    }

    /**
     * GET endpoint to retrieve all URLs in the system.
     * Returns a list of Url objects.
     */
    @GetMapping("/all")
    fun getAll(): List<Url> = urlService.getAllUrls()

}