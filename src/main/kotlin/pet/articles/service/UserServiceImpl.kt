package pet.articles.service

import org.springframework.stereotype.Service

import pet.articles.model.dto.User
import pet.articles.repository.ArticleRepository
import pet.articles.repository.AuthorshipOfArticleRepository
import pet.articles.repository.UserRepository
import pet.articles.tool.exception.DuplicateUserException

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val authorshipOfArticleRepository: AuthorshipOfArticleRepository
) : UserService {

    override fun create(user: User): User {
        validateUserUniqueness(user, null)
        return userRepository.save(user)
    }

    override fun updateById(user: User, id: Int): User {
        val targetUser: User = userRepository.findById(id)
            ?: throw NoSuchElementException("User with id $id not found")
        validateUserUniqueness(user, targetUser)
        return userRepository.updateById(user, id)
    }

    override fun deleteById(id: Int) = userRepository.deleteById(id)

    override fun findById(id: Int): User? = userRepository.findById(id)

    override fun findByUsername(username: String): User? = userRepository.findByUsername(username)

    override fun findAuthorIdsByArticleId(articleId: Int): List<Int> =
        findAuthorsByArticleId(articleId).mapNotNull(User::id)

    //изменить логику (убрать исключение)
    override fun findAuthorsByArticleId(articleId: Int): List<User> {
        if (!articleRepository.existsById(articleId)) {
            throw NoSuchElementException("Attempt to find list of users by non existent user id = $articleId")
        }
        val authorsIds: List<Int> = authorshipOfArticleRepository.findAuthorIdsByArticleId(articleId)
        return findByIds(authorsIds)
    }

    override fun findByIds(userIds: List<Int>): List<User> {
        if (userIds.isEmpty()) {
            return emptyList()
        }
        return userRepository.findByIds(userIds)
    }

    override fun findAll(): List<User> = userRepository.findAll()

    override fun existsById(id: Int): Boolean = userRepository.existsById(id)

    override fun existsByUsername(username: String): Boolean = userRepository.existsByUsername(username)

    override fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    private fun validateUserUniqueness(user: User, existingUser: User?) {
        validateUsernameUniqueness(user, existingUser)
        validateUserEmailUniqueness(user, existingUser)
    }

    private fun validateUsernameUniqueness(user: User, existingUser: User?) {
        if (existingUser != null && existingUser.username == user.username) return
        if (!existsByUsername(user.username)) return

        throw DuplicateUserException(
            "username",
            "User with username ${user.username} already exists."
        )
    }

    private fun validateUserEmailUniqueness(user: User, existingUser: User?) {
        if (existingUser != null && existingUser.email == user.email) return
        if (!existsByEmail(user.email)) return

        throw DuplicateUserException(
            "email",
            "User with email ${user.email} already exists."
        )
    }
}