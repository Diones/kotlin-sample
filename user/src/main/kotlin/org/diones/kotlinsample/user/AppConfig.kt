package org.diones.kotlinsample.user

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import org.diones.kotlinsample.user.interceptors.RestTemplateAdditionalLog
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.time.format.DateTimeFormatter


@Configuration
class AppConfig {
    @Bean
    fun restTemplate(objectMapper: ObjectMapper, restTemplateBuilder: RestTemplateBuilder): RestTemplate? {
        val jsonConverter = MappingJackson2HttpMessageConverter(objectMapper)
        return restTemplateBuilder
                .messageConverters(listOf(jsonConverter))
                .setConnectTimeout(Duration.ofSeconds(20))
                .setReadTimeout(Duration.ofSeconds(20))
                .additionalInterceptors(RestTemplateAdditionalLog())
                .build()
    }

    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder {
        return Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .dateFormat(StdDateFormat().withColonInTimeZone(true))
                .serializers(LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                        ZonedDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")))
    }
}
