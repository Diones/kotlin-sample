package org.diones.kotlinsample.card

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.diones.kotlinsample.card.events.CardEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
@EnableKafka
class KafkaConfig(@param:Value(value = "\${kafka.topics.card}") private val cardTopicName: String) {

    @Bean
    fun userTopic(): NewTopic {
        return NewTopic(cardTopicName, 1, 1.toShort())
    }

    @Bean
    fun producerFactory(kafkaProperties: KafkaProperties, objectMapper: ObjectMapper): ProducerFactory<String, Any> {
        return DefaultKafkaProducerFactory(kafkaProperties.buildProducerProperties(), StringSerializer(), JsonSerializer<Any>(objectMapper))
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Any>): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory)
    }

    @Bean
    fun consumerFactory(kafkaProperties: KafkaProperties, objectMapper: ObjectMapper): ConsumerFactory<String, CardEvent> {
        val valueDeserializer = JsonDeserializer<CardEvent>(objectMapper)
        valueDeserializer.addTrustedPackages("*")
        return DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties(), StringDeserializer(), valueDeserializer)
    }

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, CardEvent>): ConcurrentKafkaListenerContainerFactory<String, CardEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, CardEvent>()
        factory.consumerFactory = consumerFactory
        return factory
    }
}