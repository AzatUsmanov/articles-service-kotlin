package pet.articles.tool.jdbc.preparedstatement

import java.sql.PreparedStatement

data class PreparedStatementOperation<T>(
    val sqlQuery: String,
    val param: Int = PreparedStatement.NO_GENERATED_KEYS,
    val process: PreparedStatement.() -> T
)

