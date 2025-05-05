package pet.articles.test.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import pet.articles.model.dto.Article
import pet.articles.model.dto.User
import pet.articles.service.UserService
import pet.articles.tool.exception.DuplicateUserException
import pet.articles.test.tool.db.DbCleaner
import pet.articles.test.tool.generator.TestDataGenerator

import java.sql.SQLException

import java.util.NoSuchElementException


@SpringBootTest
class UserServiceTest {
    @Autowired
    private lateinit var dbCleaner: DbCleaner

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userTestDataGenerator: TestDataGenerator<User>

    @Autowired
    private lateinit var articleTestDataGenerator: TestDataGenerator<Article>

    @AfterEach
    fun cleanDb() {
        dbCleaner.cleanAll()
    }

    @Test
    fun saveUser() {
        val userForSave: User = userTestDataGenerator.generateUnsavedData()

        val savedUser: User = userService.create(userForSave)

        val userForCheck: User? = userService.findById(savedUser.id!!)
        assertNotNull(userForCheck)
        assertEquals(savedUser, userForCheck)
    }

    @Test
    fun saveUserWithInvalidData() {
        val invalidUser: User = userTestDataGenerator.generateInvalidData()
        
        assertThrows(SQLException::class.java) {
            userService.create(invalidUser)
        }
    }

    @Test
    fun saveUserWithNotUniqueUsername() {
        val savedUser: User = userTestDataGenerator.generateSavedData()
        val unsavedUser: User = userTestDataGenerator.generateUnsavedData().copy(
            username = savedUser.username
        )

        assertThrows(DuplicateUserException::class.java) {
            userService.create(unsavedUser)
        }
    }

    @Test
    fun saveUserWithNotUniqueEmail() {
        val savedUser: User = userTestDataGenerator.generateSavedData()
        val unsavedUser: User = userTestDataGenerator.generateUnsavedData().copy(
            email = savedUser.email
        )

        assertThrows(DuplicateUserException::class.java) {
            userService.create(unsavedUser)
        }
    }

    @Test
    fun updateUserById() {
        val savedUser: User = userTestDataGenerator.generateSavedData()
        val userDataForUpdate: User = userTestDataGenerator.generateUnsavedData()

        val updatedUser: User = userService.updateById(userDataForUpdate, savedUser.id!!)

        val userForCheck: User? = userService.findById(savedUser.id!!)
        assertNotNull(userForCheck)
        assertEquals(updatedUser, userForCheck)
    }

    @Test
    fun updateUserByIdWithInvalidData() {
        val invalidUser: User = userTestDataGenerator.generateInvalidData()
        val savedUser: User = userTestDataGenerator.generateSavedData()

        assertThrows(SQLException::class.java) {
            userService.updateById(invalidUser, savedUser.id!!)
        }
    }

    @Test
    fun updateUserByIdWithSameUsernameAndEmail() {
        val savedUser: User = userTestDataGenerator.generateSavedData()
        val userDataForUpdate: User = userTestDataGenerator.generateUnsavedData().copy(
            username = savedUser.username,
            email = savedUser.email
        )

        val updatedUser: User = userService.updateById(userDataForUpdate, savedUser.id!!)

        val userForCheck: User? = userService.findById(savedUser.id!!)
        assertNotNull(userForCheck)
        assertEquals(updatedUser, userForCheck)
    }

    @Test
    fun updateUserByNonExistentId() {
        val unsavedUser: User = userTestDataGenerator.generateUnsavedData()
        val userDataForUpdate: User = userTestDataGenerator.generateUnsavedData()

        assertThrows(NoSuchElementException::class.java) {
            userService.updateById(userDataForUpdate, unsavedUser.id!!)
        }
    }

    @Test
    fun updateUserByIdWithNotUniqueUsername() {
        val savedUser: User = userTestDataGenerator.generateSavedData()
        val userDataForUpdate: User = userTestDataGenerator.generateUnsavedData()
        val anotherSavedUser: User = userTestDataGenerator.generateSavedData()
        val updatedUser: User = userDataForUpdate.copy(
            username = anotherSavedUser.username
        )

        assertThrows(DuplicateUserException::class.java) {
            userService.updateById(updatedUser, savedUser.id!!)
        }
    }

    @Test
    fun updateUserByIdWithNotUniqueEmail() {
        val savedUser: User = userTestDataGenerator.generateSavedData()
        val newUserDataForUpdate: User = userTestDataGenerator.generateUnsavedData()
        val anotherSavedUser: User = userTestDataGenerator.generateSavedData()
        val updatedUser: User = newUserDataForUpdate.copy(
            email = anotherSavedUser.email
        )

        assertThrows(DuplicateUserException::class.java) {
            userService.updateById(updatedUser, savedUser.id!!)
        }
    }

    @Test
    fun deleteUserById() {
        val savedUser: User = userTestDataGenerator.generateSavedData()

        userService.deleteById(savedUser.id!!)

        assertFalse(userService.existsById(savedUser.id!!))
    }

    @Test
    fun deleteUserByNonExistentId() {
        val unsavedUser: User = userTestDataGenerator.generateUnsavedData()

        userService.deleteById(unsavedUser.id!!)

        assertFalse(userService.existsById(unsavedUser.id!!))
    }

    @Test
    fun findUserById() {
        val savedUser: User = userTestDataGenerator.generateSavedData()

        val userForCheck: User? = userService.findById(savedUser.id!!)

        assertNotNull(userForCheck)
        assertEquals(savedUser, userForCheck)
    }

    @Test
    fun findUserByNonExistentId() {
        val unsavedUser: User = userTestDataGenerator.generateUnsavedData()

        val userForCheck: User? = userService.findById(unsavedUser.id!!)

        assertNull(userForCheck)
    }

    @Test
    fun findUserByUsername() {
        val savedUser: User = userTestDataGenerator.generateSavedData()

        val userForCheck: User? = userService.findByUsername(savedUser.username)

        assertNotNull(userForCheck)
        assertEquals(savedUser, userForCheck)
    }

    @Test
    fun findUserByNonExistentUsername() {
        val unsavedUser: User = userTestDataGenerator.generateUnsavedData()

        val userForCheck: User? = userService.findByUsername(unsavedUser.username)

        assertNull(userForCheck)
    }

    @Test
    fun findAuthorsByArticleId() {
        val savedArticle: Article = articleTestDataGenerator.generateSavedData()
        val authors: List<User> = userService.findAll()

        val authorsForCheck: List<User> = userService.findAuthorsByArticleId(savedArticle.id!!)

        assertEquals(authors.size, authorsForCheck.size)
        assertTrue(authors.containsAll(authorsForCheck))
        assertTrue(authorsForCheck.containsAll(authors))
    }

    @Test
    fun findAuthorsByNonExistentArticleId() {
        val unsavedArticle: Article = articleTestDataGenerator.generateUnsavedData()

        assertThrows(NoSuchElementException::class.java) {
            userService.findAuthorsByArticleId(unsavedArticle.id!!)
        }
    }

    @Test
    fun findAllUsers() {
        val allUsers: List<User> = userTestDataGenerator.generateSavedData(10)

        val usersForCheck: List<User> = userService.findAll()

        assertEquals(allUsers.size, usersForCheck.size)
        assertTrue(allUsers.containsAll(usersForCheck))
        assertTrue(usersForCheck.containsAll(allUsers))
    }
}