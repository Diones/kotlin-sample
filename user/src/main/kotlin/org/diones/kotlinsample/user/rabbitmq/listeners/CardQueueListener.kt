package org.diones.kotlinsample.user.rabbitmq.listeners

import org.diones.kotlinsample.user.events.UserEvent
import org.diones.kotlinsample.user.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class CardQueueListener(private val userService: UserService) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["\${rabbit.queues.user}"])
    fun process(userEvent: UserEvent) {
        log.info("Processing queue $userEvent")
        val userDomain = userService.findDomainById(userEvent.id)
        // TODO - do something with this event (eg: send a request to a third-party)

        log.info("Queue processed $userEvent")
    }
}