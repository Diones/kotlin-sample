package org.diones.kotlinsample.card.controllers.json

import java.time.LocalDate

data class Card(
        val id: String,
        val bin: String,
        val last4: String,
        val expiry: LocalDate
)
