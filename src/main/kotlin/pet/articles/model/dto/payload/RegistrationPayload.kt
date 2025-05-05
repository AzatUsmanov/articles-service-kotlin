package pet.articles.model.dto.payload;

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import pet.articles.model.dto.User
import pet.articles.model.enums.UserRole

data class RegistrationPayload(
    @field:NotNull(message = UserValidation.ErrorMessages.USERNAME_NOT_NULL)
    @field:Size(
        min = UserValidation.FieldConstrains.USERNAME_MIN_LENGTH,
        max = UserValidation.FieldConstrains.PASSWORD_MAX_LENGTH,
        message = UserValidation.ErrorMessages.USERNAME_SIZE
    )
    val username : String,

    @field:NotNull(message = UserValidation.ErrorMessages.EMAIL_NOT_NULL)
    @field:Size(
        min = UserValidation.FieldConstrains.EMAIL_MIN_LENGTH,
        max = UserValidation.FieldConstrains.EMAIL_MAX_LENGTH,
        message = UserValidation.ErrorMessages.EMAIL_SIZE
    )
    @field:Email(message = "the email is in the wrong format")
    val email : String,

    @field:NotNull(message = UserValidation.ErrorMessages.PASSWORD_NOT_NULL)
    @field:Size(
        min = UserValidation.FieldConstrains.PASSWORD_MIN_LENGTH,
        max = UserValidation.FieldConstrains.PASSWORD_MAX_LENGTH,
        message = UserValidation.ErrorMessages.PASSWORD_SIZE
    )
    val password : String
) {
    fun toUser(): User = User(
        id = null,
        username = username,
        email = email,
        password = password,
        role = UserRole.ROLE_USER
    )
}
