package pet.articles.tool.jdbc.transaction;

import java.sql.Connection

interface TransactionExecutor {

    fun <T> execute(transaction: Connection.() -> T): T
}
