package org.diones.kotlinsample.card.domains

import org.diones.kotlinsample.card.controllers.json.Card
import org.diones.kotlinsample.card.controllers.json.CardPatch
import org.diones.kotlinsample.card.controllers.json.CardPut
import org.diones.kotlinsample.card.enumerators.EventType
import org.diones.kotlinsample.card.events.CardEvent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "cards")
class CardDomain(
        var bin: String,
        var last4: String,
        var expiry: LocalDate,
        @get:Id
        var id: String? = null) {

    fun toCard(): Card {
        return Card(id!!, bin, last4, expiry)
    }

    fun update(cardPatch: CardPatch): CardDomain {
        if (cardPatch.bin.orEmpty().isNotBlank()) this.bin = cardPatch.bin!!
        if (cardPatch.last4.orEmpty().isNotBlank()) this.last4 = cardPatch.last4!!
        if (cardPatch.expiry !== null) this.expiry = cardPatch.expiry
        return this
    }

    fun replace(cardPut: CardPut): CardDomain {
        this.bin = cardPut.bin
        this.last4 = cardPut.last4
        this.expiry = cardPut.expiry
        return this;
    }

    fun toCardEvent(eventType: EventType): CardEvent {
        return CardEvent(id!!, last4, expiry, eventType);
    }
}