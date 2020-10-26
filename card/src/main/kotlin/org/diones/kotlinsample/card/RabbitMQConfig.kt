package org.diones.kotlinsample.card

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

@Configuration
@EnableRabbit
class RabbitMQConfig(@param:Value("\${spring.rabbitmq.template.exchange}") private val exchange: String,
                     @param:Value("\${rabbit.queues.card}") private val cardQueue: String) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun directBindings(): Declarables {
        val directExchange = ExchangeBuilder.directExchange(exchange).build<Exchange>()
        val queueNames = listOf(cardQueue)
        val queues: MutableCollection<Declarable> = createDurableQueues(directExchange, queueNames)
        val declarable: MutableCollection<Declarable> = ArrayList()
        declarable.add(directExchange)
        declarable.addAll(queues)
        return Declarables(declarable)
    }

    @Bean
    fun jsonMessageConverter(objectMapper: ObjectMapper): MessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }

    private fun createDurableQueues(exchange: Exchange, queueNames: List<String>): MutableList<Declarable> {
        return queueNames.stream().flatMap { queueName: String ->
            val dlQueueName = "$queueName-dl"
            val dlQueue = QueueBuilder.durable(dlQueueName).build()

            log.info("Creating durable queue : {} and dead letter queue : {}", queueName, dlQueueName);
            val queue = QueueBuilder.durable(queueName).deadLetterExchange(exchange.name)
                    .deadLetterRoutingKey(dlQueueName).build()
            val dlQueueBinding = BindingBuilder.bind(dlQueue).to(exchange).with(dlQueue.name)
                    .noargs()
            val queueBinding = BindingBuilder.bind(queue).to(exchange).with(queue.name).noargs()
            Stream.of(dlQueue, queue, dlQueueBinding, queueBinding)
        }.collect(Collectors.toList())
    }
}