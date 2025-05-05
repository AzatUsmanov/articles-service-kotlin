package pet.articles.controller

import jakarta.validation.Valid

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import pet.articles.model.dto.User
import pet.articles.model.dto.payload.RegistrationPayload
import pet.articles.service.RegistrationService

@RestController
@RequestMapping(
    path = ["\${api.paths.registration}"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class RegistrationController(
    private val registrationService: RegistrationService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid registrationPayload: RegistrationPayload): User =
        registrationService.register(registrationPayload.toUser())
}