package org.diones.kotlinsample.card.interceptors

import io.opentracing.Tracer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class MDCInterceptor(@param:Value("\${interceptor.mdc-interceptor.enabled}") private val enabled: Boolean,
                     private val tracer: Tracer) : HandlerInterceptorAdapter() {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val stopWatch: ThreadLocal<StopWatch> = ThreadLocal.withInitial { StopWatch() }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, body: Any): Boolean {
        if (enabled && Objects.nonNull(tracer.activeSpan()) && Objects
                        .nonNull(tracer.activeSpan().context())) {
            stopWatch.get().start()
            val requestId = tracer.activeSpan().context().toTraceId()
            MDC.put(REQUEST_ID_HEADER_NAME, requestId)
            response.addHeader(REQUEST_ID_HEADER_NAME, requestId)

            log.info("Received request: ${request.method} ${request.requestURI}")
        }
        return super.preHandle(request, response, body)
    }

    override fun afterCompletion(request: HttpServletRequest,
                                 response: HttpServletResponse, handler: Any, ex: Exception?) {
        if (enabled) {
            val stopWatch = stopWatch.get()
            super.afterCompletion(request, response, handler, ex)
            stopWatch.stop()
            val endTime = stopWatch.totalTimeMillis

            log.info("Finished request: ${request.method} ${request.requestURI}. Time spent: ${endTime}ms")
            this.stopWatch.set(StopWatch())
            MDC.clear()
        }
    }

    companion object {
        const val REQUEST_ID_HEADER_NAME = "requestId"
    }
}