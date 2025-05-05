package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.model.dto.payload.RegistrationPayload
import pet.articles.test.tool.extension.generateRandom

@Component
class RegistrationPayloadTestDataGenerator(
    private val userTestDataGenerator: TestDataGenerator<User>
) : TestDataGenerator<RegistrationPayload> {

    override fun generateSavedData(): RegistrationPayload = TODO()

    override fun generateInvalidData(): RegistrationPayload = generateUnsavedData().copy(
        username = String.generateRandom(1000)
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