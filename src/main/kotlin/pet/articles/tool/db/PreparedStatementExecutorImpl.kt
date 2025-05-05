package pet.articles.tool.db

import org.springframework.stereotype.Component

import java.sql.Connection
import java.sql.PreparedStatement
import javax.sql.DataSource


@Component
class PreparedStatementExecutorImpl(
    private val dataSource: DataSource
) : PreparedStatementExecutor {
    override fun <T> execute(
        sqlQuery: String,
        preparedStatementParam: Int,
        configure: PreparedStatement.() -> Unit,
        process: PreparedStatement.() -> T
    ): T = dataSource.connection.use { connection ->
        connection.prepareStatement(sqlQuery, preparedStatementParam).use { statement ->
            configure(statement);
            process(statement)
        }
    }

    override fun <T> executeTransaction(
        sqlQuery: String,
        preparedStatementParam: Int,
        configure: PreparedStatement.() -> Unit,
        process: PreparedStatement.() -> T
    ): T = dataSource.connection.use { connection ->
        connection.transaction {
            connection.prepareStatement(sqlQuery, preparedStatementParam).use { statement ->
                configure(statement)
                process(statement)
            }
        }
    }

    private fun <T> Connection.transaction(block: () -> T): T {
        try {
            autoCommit = false
            val result = block()
            commit()
            return result
        } catch (e: Exception) {
            rollback()
            throw e
        } finally {
            autoCommit = true
        }
    }
}
