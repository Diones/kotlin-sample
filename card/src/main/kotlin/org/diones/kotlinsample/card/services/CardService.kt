package org.diones.kotlinsample.card.services

import org.diones.kotlinsample.card.controllers.json.Card
import org.diones.kotlinsample.card.controllers.json.CardPatch
import org.diones.kotlinsample.card.controllers.json.CardPost
import org.diones.kotlinsample.card.controllers.json.CardPut
import org.diones.kotlinsample.card.domains.CardDomain
import org.diones.kotlinsample.card.enumerators.EventType
import org.diones.kotlinsample.card.exceptions.ErrorCode
import org.diones.kotlinsample.card.exceptions.MessageError
import org.diones.kotlinsample.card.exceptions.NotFoundException
import org.diones.kotlinsample.card.kafka.producers.KafkaProducer
import org.diones.kotlinsample.card.rabbitmq.producers.RabbitProducer
import org.diones.kotlinsample.card.repositories.CardRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CardService(private val cardRepository: CardRepository, private val messageError: MessageError, private val rabbitProducer: RabbitProducer, private val kafkaProducer: KafkaProducer) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun save(cardPost: CardPost): Card {
        log.info("Save card. card=${cardPost}")
        val cardDomain = cardRepository.save(CardDomain(cardPost.bin, cardPost.last4, cardPost.expiry))
        rabbitProducer.sendToCardQueue(cardDomain.toCardEvent(EventType.ADD))
        kafkaProducer.sendToCardTopic(cardDomain.toCardEvent(EventType.ADD))
        return cardDomain.toCard()
    }

    fun findAll(offset: Int, limit: Int, sort: String?): Page<Card> {
        val sortBy = if (sort == null) Sort.unsorted() else Sort.by(sort)
        log.info("Find all cards. offset=${offset} limit=${limit} sort=${sortBy}")
        val cardDomains = cardRepository.findAll(PageRequest.of(offset, limit, sortBy))
        cardDomains.forEach { cardDomain -> rabbitProducer.sendToCardQueue(cardDomain.toCardEvent(EventType.QUERY)) }
        cardDomains.forEach { cardDomain -> kafkaProducer.sendToCardTopic(cardDomain.toCardEvent(EventType.QUERY)) }
        return cardDomains.map(CardDomain::toCard)
    }

    fun findDomainById(cardId: String): CardDomain {
        return cardRepository.findById(cardId).orElseThrow { NotFoundException(messageError.create(ErrorCode.CARD_NOT_FOUND), "Card not found! cardId=$cardId") }
    }

    fun findById(cardId: String): Card {
        log.info("Find card. cardId=${cardId}")
        val cardDomain = findDomainById(cardId)
        rabbitProducer.sendToCardQueue(cardDomain.toCardEvent(EventType.QUERY))
        kafkaProducer.sendToCardTopic(cardDomain.toCardEvent(EventType.QUERY))
        return cardDomain.toCard()
    }

    fun update(cardId: String, cardPatch: CardPatch): CardDomain {
        log.info("Update card. cardId=${cardId} newCard=${cardPatch}")
        val cardDomain = cardRepository.save(findDomainById(cardId).update(cardPatch))
        rabbitProducer.sendToCardQueue(cardDomain.toCardEvent(EventType.MODIFY))
        kafkaProducer.sendToCardTopic(cardDomain.toCardEvent(EventType.MODIFY))
        return cardDomain
    }

    fun replace(cardId: String, cardPut: CardPut): CardDomain {
        log.info("Replace card. cardId=${cardId} newCard=${cardPut}")
        val cardDomain = cardRepository.save(findDomainById(cardId).replace(cardPut))
        rabbitProducer.sendToCardQueue(cardDomain.toCardEvent(EventType.MODIFY))
        kafkaProducer.sendToCardTopic(cardDomain.toCardEvent(EventType.MODIFY))
        return cardDomain
    }

    fun delete(cardId: String) {
        log.info("Delete card. cardId=${cardId}")
        val cardDomain = findDomainById(cardId)
        rabbitProducer.sendToCardQueue(cardDomain.toCardEvent(EventType.REMOVE))
        kafkaProducer.sendToCardTopic(cardDomain.toCardEvent(EventType.REMOVE))
        cardRepository.delete(cardDomain);
    }
}