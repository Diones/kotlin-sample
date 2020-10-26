package org.diones.kotlinsample.card.serializers

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component

@Component
class RedisKeySerializer(@param:Value("\${spring.application.name}") private val appName: String) : StringRedisSerializer() {
    private val prefixKey: String = "$appName::";

    override fun serialize(value: String?): ByteArray {
        return super.serialize(prefixKey + value)
    }

    override fun deserialize(bytes: ByteArray?): String {
        return super.deserialize(bytes).replace(prefixKey, "")
    }

}