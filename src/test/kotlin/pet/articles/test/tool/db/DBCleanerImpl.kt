package pet.articles.test.tool.db

import org.springframework.stereotype.Component

import java.sql.PreparedStatement
import javax.sql.DataSource


@Component
class DBCleanerImpl(
    private val dataSource: DataSource
) : DBCleaner {

    companion object {
        private const val DELETE_ALL = "DELETE FROM %s"

        private const val REVIEWS_TABLE_NAME = "public.reviews"
        private const val AUTHORSHIP_TABLE_NAME = "public.authorship_of_articles"
        private const val ARTICLES_TABLE_NAME = "public.articles"
        private const val USERS_TABLE_NAME = "public.users"
    }

    private fun cleanTable(tableName: String) {
        dataSource.connection.use { connection ->
            connection.prepareStatement(DELETE_ALL.format(tableName))
                .use(PreparedStatement::executeUpdate)
        }
    }
    override fun cleanUp() {
        cleanTable(REVIEWS_TABLE_NAME)
        cleanTable(AUTHORSHIP_TABLE_NAME)
        cleanTable(ARTICLES_TABLE_NAME)
        cleanTable(USERS_TABLE_NAME)
    }
}