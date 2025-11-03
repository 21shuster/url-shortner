package org.example.controller

import org.example.model.Url
import org.example.service.UrlService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
            "createdAt" to savedUrl.createdAt,  // Instant timestamp
            "description" to savedUrl.description,
            "clicks" to savedUrl.clicks,
            "active" to savedUrl.active
        )
    }

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

    @DeleteMapping("/{code}")
    fun delete(@PathVariable code: String): Map<String, Any> {
        val success = urlService.deleteUrl(code)
        return if (success) {
            mapOf("message" to "URL deleted successfully")
        } else {
            mapOf("error" to "URL not found")
        }
    }

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

    @GetMapping("/all")
    fun getAll(): List<Url> = urlService.getAllUrls()

}