package org.example.controller

import org.example.model.Url
import org.example.service.UrlService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/url")
class UrlController(private val urlService: UrlService) {

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

}