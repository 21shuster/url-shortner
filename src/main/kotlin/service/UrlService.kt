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
        val url = urlRepository.findByShortCode(code).orElse(null)
        return url?.originalUrl
    }

    fun deleteUrl(code: String): Boolean {
        val urlOptional = urlRepository.findByShortCode(code)
        return if (urlOptional.isPresent) {
            val url = urlOptional.get()
            urlRepository.deleteById(url?.id)
            true
        } else {
            false
        }
    }

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

    fun incrementClicks(code: String) {
        val urlOptional = urlRepository.findByShortCode(code).orElse(null)
        urlOptional?.let {
            urlRepository.save(it.copy(
                clicks = it.clicks + 1)
            )
        }
    }

    fun deactivateUrl(code: String): Boolean {
        val urlOptional = urlRepository.findByShortCode(code)
        return if (urlOptional.isPresent) {
            val url = urlOptional.get()
            urlRepository.save(url.copy(active = false))
            true
        } else false
    }

    fun getAllUrls(): List<Url> = urlRepository.findAll()

}