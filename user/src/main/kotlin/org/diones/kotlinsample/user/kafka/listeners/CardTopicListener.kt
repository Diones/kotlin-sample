package org.diones.kotlinsample.user.kafka.listeners

import org.diones.kotlinsample.user.events.UserEvent
import org.diones.kotlinsample.user.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CardTopicListener(private val userService: UserService) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${kafka.topics.user}"])
    fun process(userEvent: UserEvent) {
        log.info("Processing topic $userEvent")
        val userDomain = userService.findDomainById(userEvent.id)
        // TODO - do something with this event (eg: send a request to a third-party)

        log.info("Topic processed $userEvent")
    }
}