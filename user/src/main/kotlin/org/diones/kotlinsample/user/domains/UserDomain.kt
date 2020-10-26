package org.diones.kotlinsample.user.domains

import org.diones.kotlinsample.user.controllers.json.User
import org.diones.kotlinsample.user.controllers.json.UserPatch
import org.diones.kotlinsample.user.controllers.json.UserPut
import org.diones.kotlinsample.user.enumerators.EventType
import org.diones.kotlinsample.user.events.UserEvent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "users")
class UserDomain(
        var name: String,
        var lastName: String,
        var birth: LocalDateTime,
        @get:Id
        var id: String? = null) {

    fun toUser(): User {
        return User(id!!, name, lastName, birth)
    }

    fun update(userPatch: UserPatch): UserDomain {
        if (userPatch.name.orEmpty().isNotBlank()) this.name = userPatch.name!!
        if (userPatch.lastName.orEmpty().isNotBlank()) this.lastName = userPatch.lastName!!
        if (userPatch.birth !== null) this.birth = userPatch.birth
        return this
    }

    fun replace(userPut: UserPut): UserDomain {
        this.name = userPut.name
        this.lastName = userPut.lastName
        this.birth = userPut.birth
        return this;
    }

    fun toUserEvent(eventType: EventType): UserEvent {
        return UserEvent(id!!, lastName, birth, eventType);
    }
}