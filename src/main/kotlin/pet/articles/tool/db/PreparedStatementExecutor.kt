package pet.articles.tool.db

import java.sql.PreparedStatement

interface PreparedStatementExecutor {

    fun <T> execute(
        sqlQuery: String,
        preparedStatementParam: Int = PreparedStatement.NO_GENERATED_KEYS,
        configure: PreparedStatement.() -> Unit = {},
        process: PreparedStatement.() -> T
    ): T

    fun <T> executeTransaction(
        sqlQuery: String,
        preparedStatementParam: Int = PreparedStatement.NO_GENERATED_KEYS,
        configure: PreparedStatement.() -> Unit,
        process: PreparedStatement.() -> T
    ): T
}