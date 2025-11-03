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

    fun getOriginalUrl(code: String): String? {
        val url = urlRepository.findById(code)
        return url.orElse(null)?.originalUrl
    }

    fun deleteUrl(code: String): Boolean {
        return if (urlRepository.existsById(code)) {
            urlRepository.deleteById(code)
            true
        } else false
    }

    fun updateUrl(code: String, newUrl: String?, newDescription: String?): Url? {
        val urlOptional = urlRepository.findById(code)
        return if (urlOptional.isPresent) {
            val url = urlOptional.get()
            val updated = url.copy(
                originalUrl = newUrl ?: url.originalUrl,
                description = newDescription ?: url.description
            )
            urlRepository.save(updated)
        } else null
    }

    fun incrementClicks(code: String) {
        val urlOptional = urlRepository.findById(code)
        if (urlOptional.isPresent) {
            val url = urlOptional.get()
            urlRepository.save(url.copy(clicks = url.clicks + 1))
        }
    }

    fun deactivateUrl(code: String): Boolean {
        val urlOptional = urlRepository.findById(code)
        return if (urlOptional.isPresent) {
            val url = urlOptional.get()
            urlRepository.save(url.copy(active = false))
            true
        } else false
    }

    fun getAllUrls(): List<Url> = urlRepository.findAll()

}