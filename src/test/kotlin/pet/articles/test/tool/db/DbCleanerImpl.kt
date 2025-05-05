package pet.articles.test.tool.db

import org.springframework.stereotype.Component

import javax.sql.DataSource

@Component
class DbCleanerImpl(
    private val dataSource: DataSource
) : DbCleaner {

    companion object {
        private const val DELETE_ALL = "TRUNCATE TABLE %s CASCADE"

        private const val REVIEWS_TABLE = "reviews"
        private const val AUTHORSHIP_TABLE = "authorship_of_articles"
        private const val ARTICLES_TABLE = "articles"
        private const val USERS_TABLE = "users"
    }

    override fun cleanAll() {
        cleanTable(REVIEWS_TABLE)
        cleanTable(AUTHORSHIP_TABLE)
        cleanTable(ARTICLES_TABLE)
        cleanTable(USERS_TABLE)
    }

    private fun cleanTable(tableName: String) {
        dataSource.connection.use { connection ->
            connection.prepareStatement(DELETE_ALL.format(tableName)).use { statement ->
                statement.executeUpdate()
            }
        }
    }
}