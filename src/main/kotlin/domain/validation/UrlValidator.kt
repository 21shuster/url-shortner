package domain.validation

import domain.exception.InvalidUrlException
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URISyntaxException

/**
 * Utility class to validate URLs before saving.
 */
@Component
object UrlValidator {

    /**
     * Validates that a string is a properly formatted URL.
     *
     * @param url URL string to validate
     * @throws InvalidUrlException if the URL is not valid
     */
    fun validateUrl(url: String) {
        try {
            val uri = URI(url) // Throws MalformedURLException if invalid
            if (uri.scheme == null || uri.host == null) {
                throw InvalidUrlException("Invalid URL: $url")
            }
        } catch (e: URISyntaxException) {
            throw InvalidUrlException("Invalid URL: $url")
        }
    }
}
