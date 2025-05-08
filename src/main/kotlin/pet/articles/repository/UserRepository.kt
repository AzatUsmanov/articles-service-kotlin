package pet.articles.repository

import jakarta.validation.constraints.NotEmpty

import pet.articles.model.dto.User

interface UserRepository {

    fun save(user: User): User

    fun updateById(user: User, id: Int): User

    fun deleteById(id: Int)

    fun findById(id: Int): User?

    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    fun findByIds(@NotEmpty ids: List<Int>): List<User>

    fun findAll(): List<User>

    fun existsById(id: Int): Boolean

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}