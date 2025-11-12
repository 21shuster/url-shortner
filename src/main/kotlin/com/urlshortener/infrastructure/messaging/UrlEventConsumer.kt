package com.urlshortener.infrastructure.messaging

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UrlEventConsumer {

    private val logger = LoggerFactory.getLogger(UrlEventConsumer::class.java)

    @KafkaListener(topics = ["url-events"], groupId = "url-shortener-group")
    fun consume(message: Map<String, Any>) {
        logger.info("📥 Received Kafka event: $message")
    }
}
