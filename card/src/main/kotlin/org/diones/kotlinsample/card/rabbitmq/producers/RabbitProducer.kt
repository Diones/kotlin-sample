package org.diones.kotlinsample.card.rabbitmq.producers

import org.diones.kotlinsample.card.events.CardEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RabbitProducer(private val rabbitTemplate: RabbitTemplate,
                     @param:Value("\${rabbit.queues.card}") private val cardQueue: String) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun sendToCardQueue(cardEvent: CardEvent) {
        rabbitTemplate.convertAndSend(cardQueue, cardEvent)
        log.info("Card event added to Queue. cardId=${cardEvent.id}");
    }
}