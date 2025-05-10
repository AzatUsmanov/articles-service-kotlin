package pet.articles.repository

import org.springframework.stereotype.Repository

import pet.articles.model.dto.Review
import pet.articles.tool.jdbc.extension.getGeneratedKey
import pet.articles.tool.jdbc.preparedstatement.PreparedStatementExecutor
import pet.articles.tool.jdbc.mapper.RowMapper
import pet.articles.tool.jdbc.preparedstatement.PreparedStatementOperation
import pet.articles.tool.jdbc.transaction.TransactionExecutor

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Timestamp

@Repository
class ReviewRepositoryImpl(
    private val reviewMapper: RowMapper<Review>,
    private val transactionExecutor: TransactionExecutor,
    private val statementExecutor: PreparedStatementExecutor
) : ReviewRepository {

    companion object {
        private const val SAVE = """
            INSERT INTO reviews(id, type, date_of_creation, content, author_id, article_id) 
            VALUES (DEFAULT, ?, ?, ?, ?, ?)
        """
        private const val DELETE_BY_ID = "DELETE FROM reviews WHERE id = ?"
        private const val FIND_BY_ID = "SELECT * FROM reviews WHERE id = ?"
        private const val FINDS_BY_AUTHOR_ID = "SELECT * FROM reviews WHERE author_id = ?"
        private const val FINDS_BY_ARTICLE_ID = "SELECT * FROM reviews WHERE article_id = ?"
    }

    override fun save(review: Review): Review =
        transactionExecutor.execute {
            val savedReviewId: Int = save(this, review)
            getById(this, savedReviewId)
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

    override fun findById(id: Int): Review? =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FIND_BY_ID,
            process = {
                setInt(1, id)
                executeQuery().use(reviewMapper::singleOrNull)
            }
        ))

    override fun findByAuthorId(authorId: Int): List<Review> =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FINDS_BY_AUTHOR_ID,
            process = {
                setInt(1, authorId)
                executeQuery().use(reviewMapper::list)
            }
        ))

    override fun findByArticleId(articleId: Int): List<Review> =
        statementExecutor.execute(PreparedStatementOperation(
            sqlQuery = FINDS_BY_ARTICLE_ID,
            process = {
                setInt(1, articleId)
                executeQuery().use(reviewMapper::list)
            }
        ))

    private fun getById(connection: Connection, id: Int): Review =
        connection.run {
            prepareStatement(FIND_BY_ID).use { preparedStatement ->
                preparedStatement.run {
                    setInt(1, id)
                    executeQuery().use(reviewMapper::singleOrNull)
                        ?: throw NoSuchElementException("Not found saved review")
                }
            }
        }

    private fun save(connection: Connection, review: Review): Int =
        connection.run {
            prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS).use { preparedStatement ->
                preparedStatement.run {
                    setInt(1, review.type.ordinal)
                    setTimestamp(2, Timestamp.valueOf(review.dateOfCreation))
                    setString(3, review.content)
                    setInt(4, review.authorId!!)
                    setInt(5, review.articleId)
                    executeUpdate()
                    getGeneratedKey()
                }
            }
        }
}