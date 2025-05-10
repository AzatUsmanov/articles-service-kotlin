package pet.articles.repository

import org.springframework.stereotype.Repository

import pet.articles.model.dto.User
import pet.articles.tool.jdbc.extension.getGeneratedKey
import pet.articles.tool.jdbc.preparedstatement.PreparedStatementExecutor
import pet.articles.tool.jdbc.mapper.RowMapper
import pet.articles.tool.jdbc.preparedstatement.PreparedStatementOperation
import pet.articles.tool.jdbc.transaction.TransactionExecutor
import pet.articles.util.SqlUtils

import java.sql.Connection
import java.sql.PreparedStatement

@Repository
class UserRepositoryImpl(
    private val userMapper: RowMapper<User>,
    private val transactionExecutor: TransactionExecutor,
    private val statementExecutor: PreparedStatementExecutor
) : UserRepository {

    companion object {
        private const val SAVE = "INSERT INTO users(id, username, email, password, role) values(DEFAULT, ?, ?, ?, ?)"
        private const val UPDATE_BY_ID = "UPDATE users SET username = ?, email= ?, password = ?, role = ? WHERE id = ?"
        private const val DELETE_BY_ID = "DELETE FROM users WHERE id = ?"
        private const val FIND_BY_ID = "SELECT * FROM users WHERE id = ?"
        private const val FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?"
        private const val FIND_BY_USERNAME = "SELECT * FROM users WHERE username = ?"
        private const val FIND_ALL = "SELECT * FROM users"
        private const val FIND_BY_IDS = "SELECT * FROM users WHERE id in (%s)"
    }

    override fun save(user: User): User =
        transactionExecutor.execute {
            val savedUserId: Int = save(this, user)
            getById(this, savedUserId)
        }


    override fun updateById(user: User, id: Int): User =
        transactionExecutor.execute {
            updateById(this, user, id)
            getById(this, id)
        }

    override fun deleteById(id: Int) =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = DELETE_BY_ID,
            process = {
                setInt(1, id)
                executeUpdate()
                Unit
            }
        ))

    override fun findById(id: Int): User? =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_BY_ID,
            process = { 
                setInt(1, id)
                executeQuery().use(userMapper::singleOrNull)
            }
        ))

    override fun findByUsername(username: String): User? =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_BY_USERNAME,
            process = { 
                setString(1, username)
                executeQuery().use(userMapper::singleOrNull)
            }
        ))

    override fun findByEmail(email: String): User? =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_BY_EMAIL,
            process = {
                setString(1, email)
                executeQuery().use(userMapper::singleOrNull) 
            }
        ))

    override fun findAll(): List<User> =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_ALL,
            process = { executeQuery().use(userMapper::list) }
        ))

    override fun findByIds(ids: List<Int>): List<User> =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = SqlUtils.buildInClause(FIND_BY_IDS, ids),
            process = { executeQuery().use(userMapper::list) }
        ))

    override fun existsById(id: Int): Boolean = findById(id) != null

    override fun existsByUsername(username: String): Boolean = findByUsername(username) != null

    override fun existsByEmail(email: String): Boolean = findByEmail(email) != null

    private fun getById(connection: Connection, id: Int): User =
        connection.run {
            prepareStatement(FIND_BY_ID).use { preparedStatement ->
                preparedStatement.run {
                    setInt(1, id)
                    executeQuery().use(userMapper::singleOrNull)
                        ?: throw NoSuchElementException("Not found saved user")
                }
            }
        }

    private fun save(connection: Connection, user: User): Int =
        connection.run {
            prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS).use { preparedStatement ->
                preparedStatement.run {
                    setString(1, user.username)
                    setString(2, user.email)
                    setString(3, user.password)
                    setInt(4, user.role.ordinal)
                    executeUpdate()
                    getGeneratedKey()
                }
            }
        }
    
    private fun updateById(connection: Connection, user: User, id: Int) =
        connection.apply {
            connection.prepareStatement(UPDATE_BY_ID).use { preparedStatement ->
                preparedStatement.apply {
                    setString(1, user.username)
                    setString(2, user.email)
                    setString(3, user.password)
                    setInt(4, user.role.ordinal)
                    setInt(5, id)
                    executeUpdate()
                }
            }
        }

}