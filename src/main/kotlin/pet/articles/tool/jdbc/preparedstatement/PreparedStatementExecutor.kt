package pet.articles.tool.jdbc.preparedstatement

interface PreparedStatementExecutor {

    fun <T> execute(operation: PreparedStatementOperation<T>): T
}