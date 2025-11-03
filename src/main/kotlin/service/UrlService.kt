package org.example.service

import org.example.model.Url
import org.example.repository.UrlRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/**
 * Service class containing business logic for URL shortening operations.
 * Handles creating, updating, retrieving, deleting, and deactivating URLs.
 */
@Service
class UrlService(private val urlRepository: UrlRepository) {

    /**
     * Shortens a given original URL by generating a unique short code.
     *
     * @param originalUrl The full URL to be shortened
     * @param description Optional description of the URL
     * @param clientIp Optional client IP address (not currently used)
     * @return Saved Url object with short code and metadata
     */
    fun shortenUrl(originalUrl: String, description: String?, clientIp: String?): Url {
        val shortCode = UUID.randomUUID().toString().substring(0, 8) // Generate 8-character short code
        val url = Url(
            shortCode = shortCode,
            originalUrl = originalUrl,
            createdAt = Instant.now(), // Use current timestamp
            description = description
        )
        return urlRepository.save(url)
    }

    /**
     * Retrieves the original URL for a given short code.
     *
     * @param code Short code of the URL
     * @return Original URL string if found, otherwise null
     */
    fun getOriginalUrl(code: String): String? {
        val url = urlRepository.findByShortCode(code).orElse(null)
        return url?.originalUrl
    }

    /**
     * Deletes a URL by its short code.
     *
     * @param code Short code of the URL to delete
     * @return True if deletion was successful, false if URL not found
     */
    fun deleteUrl(code: String): Boolean {
        val urlOptional = urlRepository.findByShortCode(code)
        return if (urlOptional.isPresent) {
            val url = urlOptional.get()
            url.id?.let { id ->
                urlRepository.deleteById(id)
            }
            true
        } else {
            false
        }
    }

    /**
     * Updates the original URL or description for a given short code.
     * If parameters are null, keeps the existing values.
     *
     * @param code Short code of the URL to update
     * @param newUrl Optional new URL value
     * @param newDescription Optional new description
     * @return Updated Url object if found, otherwise null
     */
    fun updateUrl(code: String, newUrl: String?, newDescription: String?): Url? {
        val urlOptional = urlRepository.findByShortCode(code).orElse(null)
        return urlOptional?.let {
            val updated = it.copy(
                originalUrl = newUrl ?: it.originalUrl,
                description = newDescription ?: it.description
            )
            urlRepository.save(updated)
        }
    }

    /**
     * Increments the click count of a URL by one.
     *
     * @param code Short code of the URL
     */
    fun incrementClicks(code: String) {
        val urlOptional = urlRepository.findByShortCode(code).orElse(null)
        urlOptional?.let {
            urlRepository.save(it.copy(
                clicks = it.clicks + 1) // Increment clicks
            )
        }
    }

    /**
     * Deactivates a URL, marking it as inactive.
     *
     * @param code Short code of the URL
     * @return True if successfully deactivated, false if URL not found
     */
    fun deactivateUrl(code: String): Boolean {
        val urlOptional = urlRepository.findByShortCode(code)
        return if (urlOptional.isPresent) {
            val url = urlOptional.get()
            urlRepository.save(url.copy(active = false))
            true
        } else false
    }

    /**
     * Retrieves all URLs stored in the system.
     *
     * @return List of all Url objects
     */
    fun getAllUrls(): List<Url> = urlRepository.findAll()

}