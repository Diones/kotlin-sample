package org.diones.kotlinsample.user

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import java.time.Duration
import java.util.*

@Configuration
@EnableCaching
class MultipleCacheManagerConfig(
        @param:Value("\${caffeine.ttl.default}") private val caffeineDefaultTtl: Int,
        @param:Value("\${caffeine.maximumSize.default}") private val defaultMaxSize: Int) : CachingConfigurerSupport() {
    @Bean
    @Primary
    override fun cacheManager(): CacheManager? {
        val cacheManager = CaffeineCacheManager()
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(defaultMaxSize.toLong())
                .expireAfterWrite(Duration.ofMinutes(caffeineDefaultTtl.toLong())))
        return cacheManager
    }

    @Bean
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory,
                          redisCacheDefault: RedisCacheConfiguration): CacheManager {
        val cacheNamesConfigurationMap: Map<String, RedisCacheConfiguration> = HashMap()
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheDefault)
                .withInitialCacheConfigurations(cacheNamesConfigurationMap)
                .build()
    }
}