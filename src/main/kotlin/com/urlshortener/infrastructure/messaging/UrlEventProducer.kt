package com.urlshortener.infrastructure.messaging

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class UrlEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val logger = LoggerFactory.getLogger(UrlEventProducer::class.java)
    private val topic = "url-events"

    fun publish(event: Any) {
        logger.info("📤 Sending event to Kafka: $event")
        kafkaTemplate.send(topic, event)
    }
}
