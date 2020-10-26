package org.diones.kotlinsample.user.interceptors

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class RestTemplateAdditionalLog : ClientHttpRequestInterceptor {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun intercept(request: HttpRequest, body: ByteArray,
                           execution: ClientHttpRequestExecution): ClientHttpResponse {

        log.info("Calling {} {}", request.method, request.uri);
        val response = execution.execute(request, body)
        log.info("Called {} {} - statusCode: {}", request.method, request.uri, response.statusCode);
        return execution.execute(request, body)
    }
}