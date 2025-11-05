package com.urlshortener.infrastructure.repository

import com.urlshortener.domain.Url
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository interface for performing CRUD operations on Url objects in MongoDB.
 * Extends Spring Data's MongoRepository providing standard operations such as save, findAll, delete, etc.
 */
@Repository
interface UrlRepository : MongoRepository<Url, String> {

    /**
     * Custom query method to find a Url by its short code.
     * Returns an Optional containing the Url if found, otherwise empty.
     *
     * @param shortCode the unique short code of the URL
     * @return Optional<Url> object
     */
    fun findByShortCode(shortCode: String): Url?

}