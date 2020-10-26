package org.diones.kotlinsample.card.controllers.json

import java.time.LocalDate
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class CardPatch(
        @get:Size(min = 6, max = 6, message = "{exact.size}")
        @get:Pattern(regexp = "[0-9]*", message = "{only.numbers}")
        val bin: String?,
        @get:Size(min = 4, max = 4, message = "{exact.size}")
        val last4: String?,
        val expiry: LocalDate?)
