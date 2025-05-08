package pet.articles.controller

import jakarta.validation.Valid

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import pet.articles.model.dto.User
import pet.articles.model.dto.payload.UserPayload
import pet.articles.service.UserService


@RestController
@RequestMapping(
    path = ["\${api.paths.users}"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class UserController(
    private val userService: UserService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid userPayload: UserPayload): User =
        userService.create(userPayload.toUser())

    @PatchMapping("/{id}")
    fun updateById(@RequestBody @Valid userPayload: UserPayload, @PathVariable id: Int): User =
        userService.updateById(userPayload.toUser(), id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) = userService.deleteById(id)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<User> =
        userService.findById(id)?.let { user -> ResponseEntity.ok(user) }
            ?: ResponseEntity.notFound().build()

    @GetMapping("/authorship/{articleId}")
    fun findAuthorsOfArticleId(@PathVariable articleId: Int): List<User> =
        userService.findAuthorsByArticleId(articleId)

    @GetMapping
    fun getAll(): List<User> = userService.findAll()
}