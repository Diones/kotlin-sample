package org.diones.kotlinsample.user.exceptions

enum class ErrorCode(val message: String) {
    // From 001 to 099 reserved for common validation messages
    CONTACT_SYSTEM_ADMIN("001"),
    INVALID_FIELD_VALUE("002"),
    MISSING_REQUEST_PARAM("003"),
    REQUEST_METHOD_NOT_SUPPORTED("004"),

    USER_NOT_FOUND("100")
}