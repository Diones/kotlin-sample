package org.diones.kotlinsample.card

import io.opentracing.Tracer
import io.opentracing.noop.NoopTracerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@ConditionalOnProperty(value = ["opentracing.jaeger.enabled"], havingValue = "false", matchIfMissing = false)
@Configuration
class TracerConfig {
    @Bean
    @Primary
    fun jaegerTracer(): Tracer {
        return NoopTracerFactory.create()
    }
}
