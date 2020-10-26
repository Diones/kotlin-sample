package org.diones.kotlinsample.user.kafka.producers

import org.diones.kotlinsample.user.events.UserEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(private val kafkaTemplate: KafkaTemplate<String, Any>,
                    @param:Value("\${kafka.topics.user}") private val userTopic: String) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun sendToUserTopic(userEvent: UserEvent) {
        kafkaTemplate.send(userTopic, userEvent)
        log.info("User event added to Topic. userId=${userEvent.id}");
    }
}