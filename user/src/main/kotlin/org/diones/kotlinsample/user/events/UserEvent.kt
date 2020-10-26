package org.diones.kotlinsample.user.events

import org.diones.kotlinsample.user.enumerators.EventType
import java.time.LocalDateTime

data class UserEvent(var id: String, var lastName: String, var birth: LocalDateTime, var eventType: EventType)