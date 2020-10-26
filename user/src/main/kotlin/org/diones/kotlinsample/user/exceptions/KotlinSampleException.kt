package org.diones.kotlinsample.user.exceptions

import org.diones.kotlinsample.user.exceptions.MessageError.*
import org.springframework.http.HttpStatus

open class KotlinSampleException(val status: HttpStatus, val apiErrors: List<ApiError>, detail: String?, throwable: Throwable?) : RuntimeException(detail, throwable) {
    constructor(status: HttpStatus, apiError: ApiError, detail: String?, throwable: Throwable?) : this(status, listOf(apiError), detail, throwable)

    fun getDescription(): String {
        return apiErrors.toString()
    }
}