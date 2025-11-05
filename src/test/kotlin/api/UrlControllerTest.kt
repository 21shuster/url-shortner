package api

import api.dto.UrlResponseDto
import application.UrlService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.Instant

@WebMvcTest(UrlController::class)
@ContextConfiguration(classes = [Application::class])
class UrlControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockBean
    private lateinit var urlService: UrlService

    @Test
    fun `should return health check message`() {
        mockMvc.get("/api/url/")
            .andExpect {
                status { isOk() }
                jsonPath("$.message") { value("URL Shortener API is running!") }
            }
    }

    @Test
    fun `should shorten a URL and return response`() {
        // Given
        val dto = UrlResponseDto(
            shortCode = "abc123",
            originalUrl = "https://example.com",
            description = "Example",
            createdAt = Instant.now(),
            expiresAt = null,
            accessCount = 0,
            clicks = 0,
            active = true
        )
        whenever(urlService.shortenUrl(any(), anyOrNull(), anyOrNull())).thenReturn(
            domain.Url(
                id = "1",
                originalUrl = dto.originalUrl,
                shortCode = dto.shortCode,
                createdAt = dto.createdAt,
                description = dto.description
            )
        )

        // When + Then
        mockMvc.post("/api/url/shorten") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"originalUrl": "https://example.com", "description": "Example"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.shortCode") { value("abc123") }
        }

        verify(urlService).shortenUrl("https://example.com", "Example", null)
    }

    @Test
    fun `should resolve short code`() {
        whenever(urlService.getOriginalUrl("xyz")).thenReturn("https://kotlinlang.org")

        mockMvc.get("/api/url/xyz")
            .andExpect {
                status { isOk() }
                jsonPath("$.originalUrl") { value("https://kotlinlang.org") }
            }

        verify(urlService).incrementClicks("xyz")
    }
}