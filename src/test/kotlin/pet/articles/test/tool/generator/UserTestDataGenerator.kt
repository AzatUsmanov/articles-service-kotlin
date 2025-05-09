package pet.articles.test.tool.generator

import org.instancio.Instancio
import org.instancio.Select

import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.model.dto.payload.UserValidation.FieldConstrains.PASSWORD_MAX_LENGTH
import pet.articles.model.dto.payload.UserValidation.FieldConstrains.PASSWORD_MIN_LENGTH
import pet.articles.model.dto.payload.UserValidation.FieldConstrains.USERNAME_MIN_LENGTH
import pet.articles.model.dto.payload.UserValidation.FieldConstrains.USERNAME_MAX_LENGTH
import pet.articles.service.UserService
import pet.articles.test.tool.extension.generateRandom

@Component
class UserTestDataGenerator(
    private val userService: UserService
) : TestDataGenerator<User> {

    companion object {
        const val USER_FIELD_USERNAME_NAME = "username"
        const val USER_FIELD_EMAIL_NAME = "email"
        const val USER_FIELD_PASSWORD_NAME = "password"

        const val USER_FIELD_EMAIL_PATTERN = "#a#a#a#a@#a#a#a.com"

        const val USER_FIELD_USERNAME_INVALID_LENGTH= 1000
    }

    override fun generateSavedData(): User = userService.create(generateUnsavedData())

    override fun generateInvalidData(): User = generateUnsavedData().copy(
        username = String.generateRandom(USER_FIELD_USERNAME_INVALID_LENGTH)
    )

    override fun generateUnsavedData(): User =
        Instancio
            .of(User::class.java)
            .generate(Select.field(USER_FIELD_USERNAME_NAME)) { gen ->
                gen.string()
                    .length(USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH)
                    .alphaNumeric()
            }
            .generate(Select.field(USER_FIELD_EMAIL_NAME)) { gen ->
                gen.text()
                    .pattern(USER_FIELD_EMAIL_PATTERN)
            }
            .generate(Select.field(USER_FIELD_PASSWORD_NAME)) { gen ->
                gen.string()
                    .length(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH)
                    .alphaNumeric()
            }
            .create()
}