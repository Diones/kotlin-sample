package org.diones.kotlinsample.card.events

import org.diones.kotlinsample.card.enumerators.EventType
import java.time.LocalDate

data class CardEvent(var id: String, var last4: String, var expiry: LocalDate, var eventType: EventType)