package pet.articles.repository

import org.springframework.stereotype.Repository

import pet.articles.model.dto.AuthorshipOfArticle
import pet.articles.model.enums.AuthorshipOfArticleColumn
import pet.articles.tool.jdbc.preparedstatement.PreparedStatementExecutor
import pet.articles.tool.jdbc.preparedstatement.PreparedStatementOperation

import java.sql.ResultSet


@Repository
class AuthorshipOfArticleRepositoryImpl(
    private val statementExecutor: PreparedStatementExecutor
) : AuthorshipOfArticleRepository {

    companion object {
        private const val FIND_AUTHOR_IDS = "SELECT author_id FROM authorship_of_articles WHERE article_id = ?"
        private const val FIND_ARTICLE_IDS = "SELECT article_id FROM authorship_of_articles WHERE author_id = ?"
        private const val FIND_AUTHORSHIP = """
            SELECT * FROM authorship_of_articles 
            WHERE author_id = ? AND article_id = ?
        """
    }

    override fun findAuthorIdsByArticleId(articleId: Int): List<Int> =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_AUTHOR_IDS,
            process = {
                setInt(1, articleId)
                executeQuery().use { resultSet ->
                    extractIdsFromResultSetByColumn(resultSet, AuthorshipOfArticleColumn.AUTHOR_ID)
                }
            }
        ))

    override fun findArticleIdsByAuthorId(authorId: Int): List<Int> =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_ARTICLE_IDS,
            process = {
                setInt(1, authorId)
                executeQuery().use { resultSet ->
                    extractIdsFromResultSetByColumn(resultSet, AuthorshipOfArticleColumn.ARTICLE_ID)
                }
            }
        ))

    override fun exists(authorshipOfArticle: AuthorshipOfArticle): Boolean =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_AUTHORSHIP,
            process = {
                setInt(1, authorshipOfArticle.authorId)
                setInt(2, authorshipOfArticle.articleId)
                executeQuery().use(ResultSet::next)
            }
        ))

    private fun extractIdsFromResultSetByColumn(
        resultSet: ResultSet,
        column: AuthorshipOfArticleColumn
    ): List<Int> = generateSequence {
        if (resultSet.next()) resultSet.getInt(column.columnName) else null
    }.toList()

}