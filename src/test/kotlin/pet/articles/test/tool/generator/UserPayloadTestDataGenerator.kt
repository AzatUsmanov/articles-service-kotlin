package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.model.dto.payload.UserPayload

@Component
class UserPayloadTestDataGenerator(
    private val userTestDataGenerator: TestDataGenerator<User>
) : TestDataGenerator<UserPayload> {

    override fun generateUnsavedData(dataSize: Int): List<UserPayload> =
        userTestDataGenerator.generateUnsavedData(dataSize).map(::convertToUserPayload)

    override fun generateSavedData(dataSize: Int): List<UserPayload> =
        userTestDataGenerator.generateSavedData(dataSize).map(::convertToUserPayload)

    override fun generateInvalidData(): UserPayload =
        convertToUserPayload(userTestDataGenerator.generateInvalidData())

    private fun convertToUserPayload(user: User): UserPayload =
        UserPayload(
            username = user.username,
            email = user.email,
            password = user.password!!,
            role = user.role
        )
}