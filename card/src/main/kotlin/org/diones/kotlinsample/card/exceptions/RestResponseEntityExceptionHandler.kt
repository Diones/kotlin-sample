package org.diones.kotlinsample.card.exceptions

import io.opentracing.Tracer
import io.opentracing.tag.Tags
import org.diones.kotlinsample.card.exceptions.MessageError.ApiError
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.ConversionNotSupportedException
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class RestResponseEntityExceptionHandler(private val messageError: MessageError, private val tracer: Tracer) : ResponseEntityExceptionHandler() {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val errors: MutableList<ApiError> = mutableListOf();
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val defaultMessage = error.getDefaultMessage()
            errors.add(this.messageError.create(ErrorCode.INVALID_FIELD_VALUE, fieldName, defaultMessage))
        }
        return handleKotlinSampleException(KotlinSampleException(status, errors, "Invalid field value", ex));
    }

    override fun handleHttpRequestMethodNotSupported(ex: HttpRequestMethodNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = this.messageError.create(ErrorCode.REQUEST_METHOD_NOT_SUPPORTED, ex.method)
        return handleKotlinSampleException(KotlinSampleException(status, error, "Request method not supported", ex));
    }

    override fun handleHttpMediaTypeNotSupported(ex: HttpMediaTypeNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleHttpMediaTypeNotAcceptable(ex: HttpMediaTypeNotAcceptableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleMissingPathVariable(ex: MissingPathVariableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleMissingServletRequestParameter(ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = this.messageError.create(ErrorCode.MISSING_REQUEST_PARAM, ex.parameterName, ex.parameterType)
        return handleKotlinSampleException(KotlinSampleException(status, error, "Missing request parameter", ex));
    }

    override fun handleServletRequestBindingException(ex: ServletRequestBindingException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleConversionNotSupported(ex: ConversionNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleTypeMismatch(ex: TypeMismatchException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleHttpMessageNotWritable(ex: HttpMessageNotWritableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleMissingServletRequestPart(ex: MissingServletRequestPartException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleBindException(ex: BindException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleNoHandlerFoundException(ex: NoHandlerFoundException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    override fun handleAsyncRequestTimeoutException(ex: AsyncRequestTimeoutException, headers: HttpHeaders, status: HttpStatus, webRequest: WebRequest): ResponseEntity<Any>? {
        return handleException(ex)
    }

    override fun handleExceptionInternal(ex: Exception, body: Any?, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleException(ex)
    }

    @ExceptionHandler(KotlinSampleException::class)
    fun handleKotlinSampleException(ex: KotlinSampleException): ResponseEntity<Any> {
        log.error(ex.getDescription(), ex);
        tracer.activeSpan().setTag(Tags.ERROR, true);
        return ResponseEntity.status(ex.status).body(ex.apiErrors)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<Any> {
        val error = this.messageError.create(ErrorCode.CONTACT_SYSTEM_ADMIN)
        return handleKotlinSampleException(KotlinSampleException(HttpStatus.INTERNAL_SERVER_ERROR, error, "There is no handle for this exception.", ex));
    }
}