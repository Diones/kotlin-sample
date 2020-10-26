package org.diones.kotlinsample.card.controllers

import org.diones.kotlinsample.card.controllers.json.Card
import org.diones.kotlinsample.card.controllers.json.CardPatch
import org.diones.kotlinsample.card.controllers.json.CardPost
import org.diones.kotlinsample.card.controllers.json.CardPut
import org.diones.kotlinsample.card.services.CardService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/{userId}/cards")
class CardController(private val cardService: CardService) {

    @GetMapping
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    fun getCards(@PathVariable userId: String, @RequestParam page: Int, @RequestParam size: Int, @RequestParam(required = false) sort: String?): Page<Card> {
        return cardService.findAll(page, size, sort)
    }

    @GetMapping("/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    fun getCard(@PathVariable userId: String, @PathVariable cardId: String): Card {
        return cardService.findById(cardId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCard(@PathVariable userId: String, @RequestBody @Valid cardPost: CardPost): Card {
        return cardService.save(cardPost);
    }

    @PatchMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchCard(@PathVariable userId: String, @PathVariable cardId: String, @RequestBody @Valid cardPatch: CardPatch) {
        cardService.update(cardId, cardPatch)
    }

    @PutMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putCard(@PathVariable userId: String, @PathVariable cardId: String, @RequestBody @Valid cardPut: CardPut) {
        cardService.replace(cardId, cardPut)
    }

    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCard(@PathVariable userId: String, @PathVariable cardId: String) {
        cardService.delete(cardId)
    }
}