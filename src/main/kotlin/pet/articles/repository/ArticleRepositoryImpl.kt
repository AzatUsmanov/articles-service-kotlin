package pet.articles.repository

import org.springframework.stereotype.Repository

import pet.articles.model.dto.Article
import pet.articles.tool.jdbc.extension.getGeneratedKey
import pet.articles.tool.jdbc.preparedstatement.PreparedStatementExecutor
import pet.articles.tool.jdbc.mapper.RowMapper
import pet.articles.tool.jdbc.preparedstatement.PreparedStatementOperation
import pet.articles.tool.jdbc.transaction.TransactionExecutor
import pet.articles.util.SqlUtils

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Timestamp

@Repository
class ArticleRepositoryImpl(
    private val articleMapper: RowMapper<Article>,
    private val transactionExecutor: TransactionExecutor,
    private val statementExecutor: PreparedStatementExecutor
) : ArticleRepository {

    companion object {
        private const val SAVE = """
            INSERT INTO articles(id, date_of_creation, topic, content) 
            VALUES (DEFAULT, ?, ?, ?)
        """
        private const val UPDATE_BY_ID = "UPDATE articles SET topic = ?, content = ? WHERE id = ?"
        private const val DELETE_BY_ID = "DELETE FROM articles WHERE id = ?"
        private const val FIND_BY_ID = "SELECT * FROM articles WHERE id = ?"
        private const val FIND_ALLS = "SELECT * FROM articles"
        private const val FINDS_BY_IDS = "SELECT * FROM articles WHERE id in (%s)"

        private const val SAVE_AUTHORSHIP = """
            INSERT INTO authorship_of_articles(author_id, article_id) 
            VALUES (?, ?)
        """
    }

    override fun save(article: Article, authorIds: List<Int>): Article =
        transactionExecutor.execute {
            val savedArticleId: Int = save(this, article)
            saveAuthorship(this, savedArticleId, authorIds)
            getById(this, savedArticleId)
        }

    override fun updateById(article: Article, id: Int): Article =
        transactionExecutor.execute {
            updateById(this, article, id)
            getById(this, id)
        }

    override fun deleteById(id: Int) =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = DELETE_BY_ID,
            process = {
                setInt(1, id)
                executeUpdate()
                Unit
            }
        ))

    override fun findById(id: Int): Article? =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_BY_ID,
            process = {
                setInt(1, id)
                executeQuery().use(articleMapper::singleOrNull) 
            }
        ))

    override fun findAll(): List<Article> =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_ALLS,
            process = { executeQuery().use(articleMapper::list) }
        ))

    override fun findByIds(ids: List<Int>): List<Article> {
        return statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = SqlUtils.buildInClause(FINDS_BY_IDS, ids),
            process = { executeQuery().use(articleMapper::list) }
        ))
    }

    override fun existsById(id: Int): Boolean = findById(id) != null

    private fun getById(connection: Connection, id: Int): Article =
        connection.run {
            prepareStatement(FIND_BY_ID).use { preparedStatement ->
                preparedStatement.run {
                    setInt(1, id)
                    executeQuery().use(articleMapper::singleOrNull)
                        ?: throw NoSuchElementException("Not found saved article")
                }
            }
        }

    private fun save(connection: Connection, article: Article): Int =
        connection.run {
            prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS).use { preparedStatement ->
                preparedStatement.run {
                    setTimestamp(1, Timestamp.valueOf(article.dateOfCreation))
                    setString(2, article.topic)
                    setString(3, article.content)
                    executeUpdate()
                    getGeneratedKey()
                }
            }
        }

    private fun saveAuthorship(connection: Connection, articleId: Int, authorIds: List<Int>) =
        connection.apply {
            prepareStatement(SAVE_AUTHORSHIP).use { preparedStatement ->
                preparedStatement.apply {
                    authorIds.forEach {
                        setInt(1, it)
                        setInt(2, articleId)
                        addBatch()
                    }
                    executeBatch()
                }
            }
        }

    private fun updateById(connection: Connection, article: Article, id: Int) =
        connection.apply {
            connection.prepareStatement(UPDATE_BY_ID).use { preparedStatement ->
                preparedStatement.apply {
                    setString(1, article.topic)
                    setString(2, article.content)
                    setInt(3, id)
                    executeUpdate()
                }
            }
        }
}