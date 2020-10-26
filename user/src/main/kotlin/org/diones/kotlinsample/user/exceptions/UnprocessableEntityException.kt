package org.diones.kotlinsample.user.exceptions

import org.diones.kotlinsample.user.exceptions.MessageError.ApiError
import org.springframework.http.HttpStatus

class UnprocessableEntityException(apiErrors: List<ApiError>, detail: String?, throwable: Throwable?) : KotlinSampleException(HttpStatus.UNPROCESSABLE_ENTITY, apiErrors, detail, throwable) {
    constructor(apiError: ApiError, detail: String?, throwable: Throwable?) : this(listOf(apiError), detail, throwable)
}