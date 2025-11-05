package api.dto

import java.time.Instant

/**
 * DTO de entrada para crear o actualizar una URL.
 */
data class UrlRequestDto(
    val originalUrl: String,
    val description: String? = null,
    val expiresAt: Instant? = null
)