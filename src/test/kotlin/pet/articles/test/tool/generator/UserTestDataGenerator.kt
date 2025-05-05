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

    override fun generateSavedData(): User = userService.create(generateUnsavedData())

    override fun generateInvalidData(): User = generateUnsavedData().copy(
        username = String.generateRandom(1000)
    )

    override fun generateUnsavedData(): User =
        Instancio
            .of(User::class.java)
            .generate(Select.field("username")) { gen ->
                gen.string()
                    .length(USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH)
                    .alphaNumeric()
            }
            .generate(Select.field("email")) { gen ->
                gen.text()
                    .pattern("#a#a#a#a@#a#a#a.com")
            }
            .generate(Select.field("password")) { gen ->
                gen.string()
                    .length(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH)
                    .alphaNumeric()
            }
            .create()
}