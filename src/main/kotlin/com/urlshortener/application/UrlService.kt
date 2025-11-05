package com.urlshortener.application

import com.urlshortener.domain.Url
import com.urlshortener.domain.validation.UrlValidator
import com.urlshortener.infrastructure.repository.UrlRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

/**
 * Service class containing business logic for URL shortening operations.
 */
@Service
class UrlService(
    private val urlRepository: UrlRepository,
    private val urlValidator: UrlValidator
) {

    private val logger = LoggerFactory.getLogger(UrlService::class.java)

    fun shortenUrl(originalUrl: String, description: String?, clientIp: String?): Url {
        urlValidator.validateUrl(originalUrl)
        val shortCode = UUID.randomUUID().toString().take(8)

        val url = Url(
            shortCode = shortCode,
            originalUrl = originalUrl,
            createdAt = Instant.now(),
            description = description
        )

        logger.info("Shortened URL created: $shortCode for $originalUrl")
        return urlRepository.save(url)
    }

    fun getOriginalUrl(code: String): String? =
        urlRepository.findByShortCode(code)?.originalUrl

    fun deleteUrl(code: String): Boolean {
        val url = urlRepository.findByShortCode(code)
        return if (url != null) {
            urlRepository.deleteById(url.id!!)
            logger.info("Deleted URL with code: $code")
            true
        } else false
    }

    fun updateUrl(code: String, newUrl: String?, newDescription: String?): Url? {
        val url = urlRepository.findByShortCode(code) ?: return null
        val updated = url.copy(
            originalUrl = newUrl?.also { urlValidator.validateUrl(it) } ?: url.originalUrl,
            description = newDescription ?: url.description
        )

        logger.info("Updated URL with code: $code")
        return urlRepository.save(updated)
    }

    fun incrementClicks(code: String) {
        val url = urlRepository.findByShortCode(code) ?: return
        urlRepository.save(url.copy(clicks = url.clicks + 1))
    }

    fun deactivateUrl(code: String): Boolean {
        val url = urlRepository.findByShortCode(code) ?: return false
        urlRepository.save(url.copy(active = false))
        logger.info("Deactivated URL with code: $code")
        return true
    }

    fun getAllUrls(): List<Url> = urlRepository.findAll()
}