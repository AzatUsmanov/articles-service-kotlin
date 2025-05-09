package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.model.dto.payload.UserPayload
import pet.articles.test.tool.extension.generateRandom

@Component
class UserPayloadTestDataGenerator(
    private val userTestDataGenerator: TestDataGenerator<User>
) : TestDataGenerator<UserPayload> {

    companion object {
        const val USER_FIELD_USERNAME_INVALID_LENGTH= 1000
    }

    override fun generateSavedData(): UserPayload = TODO()

    override fun generateInvalidData(): UserPayload = generateUnsavedData().copy(
        username = String.generateRandom(USER_FIELD_USERNAME_INVALID_LENGTH)
    )

    override fun generateUnsavedData(): UserPayload =
        convertToUserPayload(userTestDataGenerator.generateUnsavedData())

    private fun convertToUserPayload(user: User): UserPayload =
        UserPayload(
            username = user.username,
            email = user.email,
            password = user.password!!,
            role = user.role
        )
}