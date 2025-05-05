package pet.articles.test.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

import pet.articles.model.dto.User
import pet.articles.service.RegistrationService
import pet.articles.service.UserService

import pet.articles.tool.exception.DuplicateUserException
import pet.articles.test.tool.db.DbCleaner
import pet.articles.test.tool.generator.TestDataGenerator

import java.sql.SQLException

@SpringBootTest
class RegistrationServiceTest {
    @Autowired
    private lateinit var dbCleaner: DbCleaner

    @Autowired
    private lateinit var registrationService: RegistrationService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userTestDataGenerator: TestDataGenerator<User>

    @AfterEach
    fun cleanDB() {
        dbCleaner.cleanAll()
    }

    @Test
    fun registerUser() {
        val userForRegistration: User = userTestDataGenerator.generateUnsavedData()
        val userForRegistrationRawPassword: String = userForRegistration.password!!
        val registeredUser: User = registrationService.register(userForRegistration)

        val userForCheck: User? = userService.findById(registeredUser.id!!)
        assertNotNull(userForCheck)
        assertEquals(registeredUser, userForCheck!!)
        assertTrue(
            passwordEncoder.matches(
                userForRegistrationRawPassword,
                userForCheck.password
            )
        )
    }

    @Test
    fun registerWithInvalidData() {
        val invalidUser: User = userTestDataGenerator.generateInvalidData()

        assertThrows(SQLException::class.java) {
            registrationService.register(invalidUser)
        }
    }

    @Test
    fun registerUserWithNotUniqueUsername() {
        val savedUser: User = userTestDataGenerator.generateSavedData()
        val userForRegistration: User = userTestDataGenerator.generateUnsavedData().copy(
            username = savedUser.username
        )

        assertThrows(DuplicateUserException::class.java) {
            registrationService.register(userForRegistration)
        }
    }

    @Test
    fun registerUserWithNotUniqueEmail() {
        val savedUser: User = userTestDataGenerator.generateSavedData()
        val userForRegistration: User = userTestDataGenerator.generateUnsavedData().copy (
            email = savedUser.email
        )

        assertThrows(DuplicateUserException::class.java) {
            registrationService.register(userForRegistration)
        }
    }
}