package com.urlshortener.application

import com.urlshortener.domain.Url
import com.urlshortener.domain.validation.UrlValidator
import com.urlshortener.infrastructure.messaging.UrlEventProducer
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
    private val urlValidator: UrlValidator,
    private val eventProducer: UrlEventProducer
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

        val savedUrl = urlRepository.save(url)
        eventProducer.publish(
            mapOf(
                "event" to "UrlCreated",
                "shortCode" to savedUrl.shortCode,
                "originalUrl" to savedUrl.originalUrl,
                "description" to savedUrl.description,
                "createdAt" to savedUrl.createdAt.toString()
            )
        )

        logger.info("Shortened URL created: $shortCode for $originalUrl")
        return savedUrl
    }

    fun getOriginalUrl(code: String): String? =
        urlRepository.findByShortCode(code)?.originalUrl

    fun deleteUrl(code: String): Boolean {
        val url = urlRepository.findByShortCode(code)
        return if (url != null) {
            urlRepository.deleteById(url.id!!)
            eventProducer.publish(
                mapOf(
                    "event" to "UrlDeleted",
                    "shortCode" to url.shortCode,
                    "originalUrl" to url.originalUrl
                )
            )
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

        val saved = urlRepository.save(updated)
        eventProducer.publish(
            mapOf(
                "event" to "UrlUpdated",
                "shortCode" to saved.shortCode,
                "originalUrl" to saved.originalUrl,
                "description" to saved.description
            )
        )

        logger.info("Updated URL with code: $code")
        return saved
    }

    fun incrementClicks(code: String) {
        val url = urlRepository.findByShortCode(code) ?: return
        val updated = url.copy(clicks = url.clicks + 1)
        urlRepository.save(updated)

        eventProducer.publish(
            mapOf(
                "event" to "UrlClicked",
                "shortCode" to code,
                "clicks" to updated.clicks
            )
        )

        logger.info("Incremented clicks for URL code: $code")
    }

    fun deactivateUrl(code: String): Boolean {
        val url = urlRepository.findByShortCode(code) ?: return false
        val deactivated = url.copy(active = false)
        urlRepository.save(deactivated)

        eventProducer.publish(
            mapOf(
                "event" to "UrlDeactivated",
                "shortCode" to code,
                "originalUrl" to url.originalUrl
            )
        )

        logger.info("Deactivated URL with code: $code")
        return true
    }

    fun getAllUrls(): List<Url> = urlRepository.findAll()
}