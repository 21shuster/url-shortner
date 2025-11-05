package com.urlshortener.api.dto

import com.urlshortener.domain.Url
import java.time.Instant

/**
 * DTO de salida para devolver información de una URL.
 */
data class UrlResponseDto(
    val shortCode: String,
    val originalUrl: String,
    val description: String?,
    val createdAt: Instant,
    val expiresAt: Instant?,
    val accessCount: Long,
    val clicks: Int,
    val active: Boolean
) {
    companion object {
        fun fromDomain(url: Url): UrlResponseDto = UrlResponseDto(
            shortCode = url.shortCode,
            originalUrl = url.originalUrl,
            description = url.description,
            createdAt = url.createdAt,
            expiresAt = url.expiresAt,
            accessCount = url.accessCount,
            clicks = url.clicks,
            active = url.active
        )
    }
}