package pet.articles.test.tool.producer

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.model.enums.UserRole
import pet.articles.service.RegistrationService
import pet.articles.test.tool.generator.TestDataGenerator

@Component
class AuthenticationDetailsProducerImpl(
    private val userToUserDetailsConverter: Converter<User, UserDetails>,
    private val userTestDataGenerator: TestDataGenerator<User>,
    private val registrationService: RegistrationService
) : AuthenticationDetailsProducer {

    override fun produceUserDetailsOfRegisteredUser(role: UserRole): UserDetails =
        userToUserDetailsConverter.convert(produceRegisteredUserWithRawPassword(role))
            ?: throw IllegalStateException("Conversion to UserDetails failed")

    override fun produceRegisteredUserWithRawPassword(role: UserRole): User {
        val unsavedUser = userTestDataGenerator.generateUnsavedData().copy(
            role = role
        )
        return registrationService.register(unsavedUser).copy(
            password = unsavedUser.password
        )
    }
}