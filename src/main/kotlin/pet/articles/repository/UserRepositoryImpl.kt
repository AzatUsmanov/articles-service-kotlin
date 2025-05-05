package pet.articles.repository

import org.springframework.stereotype.Repository

import pet.articles.model.dto.User
import pet.articles.tool.db.PreparedStatementExecutor
import pet.articles.tool.db.RowMapper
import pet.articles.util.SqlUtils

import java.sql.PreparedStatement

@Repository
class UserRepositoryImpl(
    private val statementExecutor: PreparedStatementExecutor,
    private val userMapper: RowMapper<User>
) : UserRepository {

    companion object {
        private const val SAVE_USER = "INSERT INTO users(id, username, email, password, role) values(DEFAULT, ?, ?, ?, ?)"
        private const val UPDATE_USER_BY_ID = "UPDATE users SET username = ?, email= ?, password = ?, role = ? WHERE id = ?"
        private const val DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?"
        private const val FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ?"
        private const val FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?"
        private const val FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?"
        private const val FIND_ALL_USERS = "SELECT * FROM users"
        private const val FIND_USERS_BY_IDS = "SELECT * FROM users WHERE id in (%s)"
    }

    override fun save(user: User): User =
        statementExecutor.execute(
            sqlQuery = SAVE_USER,
            preparedStatementParam = PreparedStatement.RETURN_GENERATED_KEYS,
            configure = {
                setString(1, user.username)
                setString(2, user.email)
                setString(3, user.password)
                setInt(4, user.role.ordinal)
            },
            process = {
                executeUpdate()
                generatedKeys.use { keys ->
                    if (!keys.next()) {
                        throw RuntimeException("Failed to get generated ID")
                    }
                    val savedUserId: Int = keys.getInt(1)
                    findById(savedUserId) ?: throw NoSuchElementException("User not found after save")
                }
            }
        )

    override fun updateById(user: User, id: Int): User =
        statementExecutor.execute(
            sqlQuery = UPDATE_USER_BY_ID,
            configure = {
                setString(1, user.username)
                setString(2, user.email)
                setString(3, user.password)
                setInt(4, user.role.ordinal)
                setInt(5, id)
            },
            process = {
                executeUpdate()
                findById(id) ?: throw NoSuchElementException("User not found after update")
            }
        )

    override fun deleteById(id: Int) =
        statementExecutor.execute(
            sqlQuery = DELETE_USER_BY_ID,
            configure = { setInt(1, id) },
            process = {
                executeUpdate()
                Unit
            }
        )

    override fun existsById(id: Int): Boolean = findById(id) != null

    override fun existsByUsername(username: String): Boolean = findByUsername(username) != null

    override fun existsByEmail(email: String): Boolean = findByEmail(email) != null

    override fun findById(id: Int): User? =
        statementExecutor.execute(
            sqlQuery = FIND_USER_BY_ID,
            configure = { setInt(1, id) },
            process = { executeQuery().use(userMapper::singleOrNull) }
        )

    override fun findByUsername(username: String): User? =
        statementExecutor.execute(
            sqlQuery = FIND_USER_BY_USERNAME,
            configure = { setString(1, username) },
            process = { executeQuery().use(userMapper::singleOrNull) }
        )

    override fun findByEmail(email: String): User? =
        statementExecutor.execute(
            sqlQuery = FIND_USER_BY_EMAIL,
            configure = { setString(1, email) },
            process = { executeQuery().use(userMapper::singleOrNull) }
        )

    override fun findAll(): List<User> =
        statementExecutor.execute(
            sqlQuery = FIND_ALL_USERS,
            process = { executeQuery().use(userMapper::list) }
        )

    override fun findByIds(ids: List<Int>): List<User> {
        val sqlQuery = SqlUtils.buildInClause(FIND_USERS_BY_IDS, ids)
        return statementExecutor.execute(
            sqlQuery = sqlQuery,
            process = { executeQuery().use(userMapper::list) }
        )
    }
}