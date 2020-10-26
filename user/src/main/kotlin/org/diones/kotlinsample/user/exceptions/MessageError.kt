package org.diones.kotlinsample.user.exceptions

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class MessageError(private val messageSource: MessageSource) {

    fun create(code: ErrorCode, vararg replacements: String?): ApiError {
        return ApiError(code.message.toInt(), messageSource.getMessage(code.message, replacements, LocaleContextHolder.getLocale()))
    }

    data class ApiError(val code: Int, val message: String)
}