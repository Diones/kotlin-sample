package org.diones.kotlinsample.card.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(apiErrors: List<MessageError.ApiError>, detail: String?) : KotlinSampleException(HttpStatus.NOT_FOUND, apiErrors, detail, null) {
    constructor(apiError: MessageError.ApiError, detail: String?) : this(listOf(apiError), detail)
}