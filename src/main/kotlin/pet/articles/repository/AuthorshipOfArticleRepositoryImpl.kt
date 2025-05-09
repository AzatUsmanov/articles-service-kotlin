package pet.articles.repository

import org.springframework.stereotype.Repository

import pet.articles.model.dto.AuthorshipOfArticle
import pet.articles.model.enums.AuthorshipOfArticleColumn
import pet.articles.tool.db.PreparedStatementExecutor
import java.sql.ResultSet


@Repository
class AuthorshipOfArticleRepositoryImpl(
    private val statementExecutor: PreparedStatementExecutor
) : AuthorshipOfArticleRepository {

    companion object {
        private const val SAVE_AUTHORSHIP = """
            INSERT INTO authorship_of_articles(author_id, article_id) 
            VALUES (?, ?)
        """
        private const val FIND_AUTHOR_IDS = "SELECT author_id FROM authorship_of_articles WHERE article_id = ?"
        private const val FIND_ARTICLE_IDS = "SELECT article_id FROM authorship_of_articles WHERE author_id = ?"
        private const val FIND_AUTHORSHIP = """
            SELECT * FROM authorship_of_articles 
            WHERE author_id = ? AND article_id = ?
        """
    }

    override fun save(authorshipOfArticles: List<AuthorshipOfArticle>): List<AuthorshipOfArticle> =
        statementExecutor.executeTransaction(
            sqlQuery = SAVE_AUTHORSHIP,
            configure = {
                for ((authorId, articleId) in authorshipOfArticles) {
                    setInt(1, authorId)
                    setInt(2, articleId)
                    addBatch()
                }
            },
            process = {
                executeBatch()
                authorshipOfArticles
            }
        )

    override fun findAuthorIdsByArticleId(articleId: Int): List<Int> =
        statementExecutor.execute(
            sqlQuery = FIND_AUTHOR_IDS,
            configure = { setInt(1, articleId) },
            process = { executeQuery().use { resultSet ->
                generateSequence {
                    if (resultSet.next()) resultSet.getInt(AuthorshipOfArticleColumn.AUTHOR_ID.columnName) else null
                }.toList()
            }}
        )

    override fun findArticleIdsByAuthorId(authorId: Int): List<Int> =
        statementExecutor.execute(
            sqlQuery = FIND_ARTICLE_IDS,
            configure = { setInt(1, authorId) },
            process = { executeQuery().use { resultSet ->
                generateSequence {
                    if (resultSet.next()) resultSet.getInt(AuthorshipOfArticleColumn.ARTICLE_ID.columnName) else null
                }.toList()
            }}
        )

    override fun exists(authorshipOfArticle: AuthorshipOfArticle): Boolean =
        statementExecutor.execute(
            sqlQuery = FIND_AUTHORSHIP,
            configure = {
                setInt(1, authorshipOfArticle.authorId)
                setInt(2, authorshipOfArticle.articleId)
            },
            process = { executeQuery().use(ResultSet::next) }
        )
}