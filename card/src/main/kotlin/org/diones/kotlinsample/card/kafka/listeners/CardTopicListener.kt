package org.diones.kotlinsample.card.kafka.listeners

import org.diones.kotlinsample.card.events.CardEvent
import org.diones.kotlinsample.card.services.CardService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CardTopicListener(private val cardService: CardService) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${kafka.topics.card}"])
    fun process(cardEvent: CardEvent) {
        log.info("Processing topic $cardEvent")
        val cardDomain = cardService.findDomainById(cardEvent.id)
        // TODO - do something with this event (eg: send a request to a third-party)

        log.info("Topic processed $cardEvent")
    }
}