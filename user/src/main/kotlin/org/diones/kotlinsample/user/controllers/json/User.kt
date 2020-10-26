package org.diones.kotlinsample.user.controllers.json

import java.time.LocalDateTime

data class User(
        val id: String,
        val name: String,
        val lastName: String,
        val birth: LocalDateTime
)
