package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.model.dto.payload.RegistrationPayload
import pet.articles.test.tool.extension.generateRandom

@Component
class RegistrationPayloadTestDataGenerator(
    private val userTestDataGenerator: TestDataGenerator<User>
) : TestDataGenerator<RegistrationPayload> {

    companion object {
        const val USER_FIELD_USERNAME_INVALID_LENGTH= 1000
    }

    override fun generateSavedData(): RegistrationPayload = TODO()

    override fun generateInvalidData(): RegistrationPayload = generateUnsavedData().copy(
        username = String.generateRandom(USER_FIELD_USERNAME_INVALID_LENGTH)
    )

    override fun generateUnsavedData(): RegistrationPayload =
        convertToRegistrationPayload(userTestDataGenerator.generateUnsavedData())

    private fun convertToRegistrationPayload(user: User): RegistrationPayload =
        RegistrationPayload(
            username = user.username,
            email = user.email,
            password = user.password!!
        )
}