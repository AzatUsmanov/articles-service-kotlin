package pet.articles.tool.jdbc.transaction

import org.springframework.stereotype.Component

import java.sql.Connection
import javax.sql.DataSource

@Component
class TransactionExecutorImpl(
    private val dataSource: DataSource
): TransactionExecutor {

    override fun <T> execute(transaction: Connection.() -> T): T {
        dataSource.connection.use { connection ->
            connection.apply {
                try {
                    autoCommit = false
                    val result: T = transaction()
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
    }
}