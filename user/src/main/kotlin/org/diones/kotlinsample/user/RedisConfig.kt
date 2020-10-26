package org.diones.kotlinsample.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.diones.kotlinsample.user.serializers.RedisKeySerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig(@param:Value("\${redis.ttl.default}") private val defaultTtl: Int,
                  private val redisKeySerializer: RedisKeySerializer) {
    @Bean
    fun redisCacheDefault(): RedisCacheConfiguration {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(defaultTtl.toLong()))
                .disableCachingNullValues()
    }

    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory,
                      objectMapper: ObjectMapper): RedisTemplate<String, Any> {
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(
                Any::class.java)
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper)
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(connectionFactory)
        template.valueSerializer = jackson2JsonRedisSerializer
        template.keySerializer = redisKeySerializer
        template.afterPropertiesSet()
        return template
    }
}