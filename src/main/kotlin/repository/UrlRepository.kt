package org.example.repository

import org.example.model.Url
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UrlRepository : MongoRepository<Url, String> {
    fun findByShortCode(shortCode: String): Optional<Url>
}