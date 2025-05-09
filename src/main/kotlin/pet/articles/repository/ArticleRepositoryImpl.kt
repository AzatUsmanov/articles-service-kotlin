package pet.articles.repository

import org.springframework.stereotype.Repository

import pet.articles.model.dto.Article
import pet.articles.tool.db.PreparedStatementExecutor
import pet.articles.tool.db.RowMapper
import pet.articles.util.SqlUtils

import java.sql.PreparedStatement
import java.sql.Timestamp

@Repository
class ArticleRepositoryImpl(
    private val statementExecutor: PreparedStatementExecutor,
    private val articleMapper: RowMapper<Article>
) : ArticleRepository {

    companion object {
        private const val SAVE_ARTICLE = """
            INSERT INTO articles(id, date_of_creation, topic, content) 
            VALUES (DEFAULT, ?, ?, ?)
        """
        private const val UPDATE_ARTICLE_BY_ID = "UPDATE articles SET topic = ?, content = ? WHERE id = ?"
        private const val DELETE_ARTICLE_BY_ID = "DELETE FROM articles WHERE id = ?"
        private const val FIND_ARTICLE_BY_ID = "SELECT * FROM articles WHERE id = ?"
        private const val FIND_ALL_ARTICLES = "SELECT * FROM articles"
        private const val FIND_ARTICLES_BY_IDS = "SELECT * FROM articles WHERE id in (%s)"
    }

    override fun save(article: Article): Article =
        statementExecutor.execute(
            sqlQuery = SAVE_ARTICLE,
            preparedStatementParam = PreparedStatement.RETURN_GENERATED_KEYS,
            configure = {
                setTimestamp(1, Timestamp.valueOf(article.dateOfCreation))
                setString(2, article.topic)
                setString(3, article.content)
            },
            process = {
                executeUpdate()
                generatedKeys.use { keys ->
                    if (!keys.next()) {
                        throw RuntimeException("Failed to get generated ID")
                    }
                    val savedArticleId: Int = keys.getInt(1)
                    findById(savedArticleId) ?: throw NoSuchElementException("Article not found after save")
                }
            }
        )

    override fun updateById(article: Article, id: Int): Article =
        statementExecutor.execute(
            sqlQuery = UPDATE_ARTICLE_BY_ID,
            configure = {
                setString(1, article.topic)
                setString(2, article.content)
                setInt(3, id)
            },
            process = {
                executeUpdate()
                findById(id) ?: throw NoSuchElementException("Article not found after update")
            }
        )

    override fun deleteById(id: Int) =
        statementExecutor.execute(
            sqlQuery = DELETE_ARTICLE_BY_ID,
            configure = { setInt(1, id) },
            process = {
                executeUpdate()
                Unit
            }
        )

    override fun findById(id: Int): Article? =
        statementExecutor.execute(
            sqlQuery = FIND_ARTICLE_BY_ID,
            configure = { setInt(1, id) },
            process = { executeQuery().use(articleMapper::singleOrNull) }
        )

    override fun findAll(): List<Article> =
        statementExecutor.execute(
            sqlQuery = FIND_ALL_ARTICLES,
            process = { executeQuery().use(articleMapper::list) }
        )

    override fun findByIds(ids: List<Int>): List<Article> {
        val sqlQuery = SqlUtils.buildInClause(FIND_ARTICLES_BY_IDS, ids)
        return statementExecutor.execute(
            sqlQuery = sqlQuery,
            process = { executeQuery().use(articleMapper::list) }
        )
    }

    override fun existsById(id: Int): Boolean = findById(id) != null
}