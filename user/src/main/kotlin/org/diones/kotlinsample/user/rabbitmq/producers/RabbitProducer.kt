package org.diones.kotlinsample.user.rabbitmq.producers

import org.diones.kotlinsample.user.events.UserEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RabbitProducer(private val rabbitTemplate: RabbitTemplate,
                     @param:Value("\${rabbit.queues.user}") private val userQueue: String) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun sendToUserQueue(userEvent: UserEvent) {
        rabbitTemplate.convertAndSend(userQueue, userEvent)
        log.info("Card event added to Queue. userId=${userEvent.id}");
    }
}