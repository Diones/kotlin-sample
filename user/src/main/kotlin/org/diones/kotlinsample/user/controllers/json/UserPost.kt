package org.diones.kotlinsample.user.controllers.json

import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class UserPost(
        @get:Size(min = 3, max = 20)
        val name: String,
        @get:Size(min = 3, max = 80)
        val lastName: String,
        val birth: LocalDateTime)
