package org.diones.kotlinsample.user.services

import org.diones.kotlinsample.user.controllers.json.User
import org.diones.kotlinsample.user.controllers.json.UserPatch
import org.diones.kotlinsample.user.controllers.json.UserPost
import org.diones.kotlinsample.user.controllers.json.UserPut
import org.diones.kotlinsample.user.domains.UserDomain
import org.diones.kotlinsample.user.enumerators.EventType
import org.diones.kotlinsample.user.exceptions.ErrorCode
import org.diones.kotlinsample.user.exceptions.MessageError
import org.diones.kotlinsample.user.exceptions.NotFoundException
import org.diones.kotlinsample.user.kafka.producers.KafkaProducer
import org.diones.kotlinsample.user.rabbitmq.producers.RabbitProducer
import org.diones.kotlinsample.user.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val messageError: MessageError, private val rabbitProducer: RabbitProducer, private val kafkaProducer: KafkaProducer) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun save(userPost: UserPost): User {
        log.info("Save user. user=${userPost}")
        val userDomain = userRepository.save(UserDomain(userPost.name, userPost.lastName, userPost.birth))
        rabbitProducer.sendToUserQueue(userDomain.toUserEvent(EventType.ADD))
        kafkaProducer.sendToUserTopic(userDomain.toUserEvent(EventType.ADD))
        return userDomain.toUser()
    }

    fun findAll(offset: Int, limit: Int, sort: String?): Page<User> {
        val sortBy = if (sort == null) Sort.unsorted() else Sort.by(sort)
        log.info("Find all users. offset=${offset} limit=${limit} sort=${sortBy}")
        val userDomains = userRepository.findAll(PageRequest.of(offset, limit, sortBy))
        userDomains.forEach { userDomain -> rabbitProducer.sendToUserQueue(userDomain.toUserEvent(EventType.QUERY)) }
        userDomains.forEach { userDomain -> kafkaProducer.sendToUserTopic(userDomain.toUserEvent(EventType.QUERY)) }
        return userDomains.map(UserDomain::toUser)
    }

    fun findDomainById(userId: String): UserDomain {
        return userRepository.findById(userId).orElseThrow { NotFoundException(messageError.create(ErrorCode.USER_NOT_FOUND), "User not found! userId=$userId") }
    }

    fun findById(userId: String): User {
        log.info("Find user. userId=${userId}")
        val userDomain = findDomainById(userId)
        rabbitProducer.sendToUserQueue(userDomain.toUserEvent(EventType.QUERY))
        kafkaProducer.sendToUserTopic(userDomain.toUserEvent(EventType.QUERY))
        return userDomain.toUser()
    }

    fun update(userId: String, userPatch: UserPatch): UserDomain {
        log.info("Update user. userId=${userId} newUser=${userPatch}")
        val userDomain = userRepository.save(findDomainById(userId).update(userPatch))
        rabbitProducer.sendToUserQueue(userDomain.toUserEvent(EventType.MODIFY))
        kafkaProducer.sendToUserTopic(userDomain.toUserEvent(EventType.MODIFY))
        return userDomain
    }

    fun replace(userId: String, userPut: UserPut): UserDomain {
        log.info("Replace user. userId=${userId} newUser=${userPut}")
        val userDomain = userRepository.save(findDomainById(userId).replace(userPut))
        rabbitProducer.sendToUserQueue(userDomain.toUserEvent(EventType.MODIFY))
        kafkaProducer.sendToUserTopic(userDomain.toUserEvent(EventType.MODIFY))
        return userDomain
    }

    fun delete(userId: String) {
        log.info("Delete user. userId=${userId}")
        val userDomain = findDomainById(userId)
        rabbitProducer.sendToUserQueue(userDomain.toUserEvent(EventType.REMOVE))
        kafkaProducer.sendToUserTopic(userDomain.toUserEvent(EventType.REMOVE))
        userRepository.delete(userDomain);
    }
}