package org.diones.kotlinsample.card.rabbitmq.listeners

import org.diones.kotlinsample.card.events.CardEvent
import org.diones.kotlinsample.card.services.CardService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class CardQueueListener(private val cardService: CardService) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["\${rabbit.queues.card}"])
    fun process(cardEvent: CardEvent) {
        log.info("Processing queue $cardEvent")
        val cardDomain = cardService.findDomainById(cardEvent.id)
        // TODO - do something with this event (eg: send a request to a third-party)

        log.info("Queue processed $cardEvent")
    }
}