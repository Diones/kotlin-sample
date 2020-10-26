package org.diones.kotlinsample.user.controllers

import org.diones.kotlinsample.user.controllers.json.User
import org.diones.kotlinsample.user.controllers.json.UserPatch
import org.diones.kotlinsample.user.controllers.json.UserPost
import org.diones.kotlinsample.user.controllers.json.UserPut
import org.diones.kotlinsample.user.services.UserService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    fun getCards(@RequestParam page: Int, @RequestParam size: Int, @RequestParam(required = false) sort: String?): Page<User> {
        return userService.findAll(page, size, sort)
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getCard(@PathVariable userId: String): User {
        return userService.findById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCard(@RequestBody @Valid userPost: UserPost): User {
        return userService.save(userPost);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchCard(@PathVariable userId: String, @RequestBody @Valid userPatch: UserPatch) {
        userService.update(userId, userPatch)
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putCard(@PathVariable userId: String, @RequestBody @Valid userPut: UserPut) {
        userService.replace(userId, userPut)
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCard(@PathVariable userId: String) {
        userService.delete(userId)
    }
}