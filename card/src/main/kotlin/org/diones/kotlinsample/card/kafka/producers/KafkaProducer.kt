package org.diones.kotlinsample.card.kafka.producers

import org.diones.kotlinsample.card.events.CardEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(private val kafkaTemplate: KafkaTemplate<String, Any>,
                    @param:Value("\${kafka.topics.card}") private val cardTopic: String) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun sendToCardTopic(cardEvent: CardEvent) {
        kafkaTemplate.send(cardTopic, cardEvent)
        log.info("Card event added to Topic. cardId=${cardEvent.id}");
    }
}