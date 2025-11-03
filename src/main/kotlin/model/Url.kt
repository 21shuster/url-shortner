package org.example.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

/**
 * Represents a shortened URL document stored in MongoDB.
 * Each document contains information about the original URL,
 * its shortened code, metadata, and usage statistics.
 */
@Document(collection = "urls")
data class Url(
    /** MongoDB auto-generated document ID */
    @Id
    val id: String? = null,
    /** The full original URL entered by the user */
    val originalUrl: String,
    /** The short code that identifies this URL */
    val shortCode: String,
    /** The date and time when this short URL was created */
    val createdAt: Instant = Instant.now(),
    /** Optional expiration date for the short link (null means never expires) */
    val expiresAt: Instant? = null,
    /** The total number of times the short URL has been accessed */
    val accessCount: Long = 0,
    /** The IP address of the creator (optional, for analytics or logging) */
    val createdByIp: String? = null,
    /** Optional title or description for the link */
    val description: String? = null,
    /** Number of times URL was accessed */
    val clicks: Int = 0,
    /** Flag to indicate if the link is active (false = disabled/deleted) */
    val active: Boolean = true
)