package pet.articles.model.dto.payload

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import pet.articles.model.dto.User
import pet.articles.model.enums.UserRole

object UserValidation {

    object FieldConstrains {
        const val USERNAME_MIN_LENGTH = 5
        const val USERNAME_MAX_LENGTH = 30
        const val EMAIL_MIN_LENGTH = 5
        const val EMAIL_MAX_LENGTH = 50
        const val PASSWORD_MIN_LENGTH = 5
        const val PASSWORD_MAX_LENGTH = 100
    }

    object ErrorMessages {
        const val USERNAME_NOT_NULL = "username must be not null"
        const val USERNAME_SIZE = "the length of the username must be between {min} and {max}"
        const val EMAIL_NOT_NULL = "email must be not null"
        const val EMAIL_SIZE = "the length of the email must be between {min} and {max}"
        const val EMAIL_INVALID_FORMAT = "the email is in the wrong format"
        const val PASSWORD_NOT_NULL = "password must be not null"
        const val PASSWORD_SIZE = "the length of the password must be between {min} and {max}"
        const val ROLE_NOT_NULL = "User role must be not null"
    }
}

data class UserPayload(
    @field:NotNull(message = UserValidation.ErrorMessages.USERNAME_NOT_NULL)
    @field:Size(
        min = UserValidation.FieldConstrains.USERNAME_MIN_LENGTH,
        max = UserValidation.FieldConstrains.USERNAME_MAX_LENGTH,
        message = UserValidation.ErrorMessages.USERNAME_SIZE
    )
    val username : String,

    @field:NotNull(message = UserValidation.ErrorMessages.EMAIL_NOT_NULL)
    @field:Size(
        min = UserValidation.FieldConstrains.EMAIL_MIN_LENGTH,
        max = UserValidation.FieldConstrains.EMAIL_MAX_LENGTH,
        message = UserValidation.ErrorMessages.EMAIL_SIZE
    )
    @field:Email(message = UserValidation.ErrorMessages.EMAIL_INVALID_FORMAT)
    val email : String,

    @field:NotNull(message = UserValidation.ErrorMessages.PASSWORD_NOT_NULL)
    @field:Size(
        min = UserValidation.FieldConstrains.PASSWORD_MIN_LENGTH,
        max = UserValidation.FieldConstrains.PASSWORD_MAX_LENGTH,
        message = UserValidation.ErrorMessages.PASSWORD_SIZE
    )
    val password : String,

    @field:NotNull(message = UserValidation.ErrorMessages.ROLE_NOT_NULL)
    val role : UserRole
) {
    fun toUser(): User = User(
        id = null,
        username = username,
        email = email,
        password = password,
        role = role
    )
}


