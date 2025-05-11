package pet.articles.tool.jdbc.preparedstatement

import org.springframework.stereotype.Component

import javax.sql.DataSource

@Component
class PreparedStatementExecutorImpl(
    private val dataSource: DataSource
) : PreparedStatementExecutor {

    override fun <T> execute(operation: PreparedStatementOperation<T>): T =
        runCatching {
            dataSource.connection.use {
                it.prepareStatement(operation.sqlQuery, operation.param)
                    .use(operation.process)
            }
        }.getOrElse { throw RuntimeException(it) }
}