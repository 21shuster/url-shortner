package org.example.service

import org.example.model.Url
import org.example.repository.UrlRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class UrlService(private val urlRepository: UrlRepository) {

    fun shortenUrl(originalUrl: String, description: String?, clientIp: String?): Url {
        val shortCode = UUID.randomUUID().toString().substring(0, 8)
        val url = Url(
            shortCode = shortCode,
            originalUrl = originalUrl,
            createdAt = Instant.now(),
            description = description
        )
        return urlRepository.save(url)
    }

}