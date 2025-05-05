package pet.articles.repository
import org.springframework.stereotype.Repository

import pet.articles.model.dto.Review
import pet.articles.tool.db.PreparedStatementExecutor
import pet.articles.tool.db.RowMapper

import java.sql.PreparedStatement
import java.sql.Timestamp

@Repository
class ReviewRepositoryImpl(
    private val statementExecutor: PreparedStatementExecutor,
    private val reviewMapper: RowMapper<Review>
) : ReviewRepository {

    companion object {
        private const val SAVE_REVIEW = """
            INSERT INTO reviews(id, type, date_of_creation, content, author_id, article_id) 
            VALUES (DEFAULT, ?, ?, ?, ?, ?)
        """
        private const val DELETE_REVIEW_BY_ID = "DELETE FROM reviews WHERE id = ?"
        private const val FIND_REVIEW_BY_ID = "SELECT * FROM reviews WHERE id = ?"
        private const val FIND_REVIEWS_BY_AUTHOR_ID = "SELECT * FROM reviews WHERE author_id = ?"
        private const val FIND_REVIEWS_BY_ARTICLE_ID = "SELECT * FROM reviews WHERE article_id = ?"
    }

    override fun save(review: Review): Review =
        statementExecutor.execute(
            sqlQuery = SAVE_REVIEW,
            preparedStatementParam = PreparedStatement.RETURN_GENERATED_KEYS,
            configure = {  
                setInt(1, review.type.ordinal)
                setTimestamp(2, Timestamp.valueOf(review.dateOfCreation))
                setString(3, review.content)
                setInt(4, review.authorId)
                setInt(5, review.articleId)
            },
            process = {  
                executeUpdate()
                generatedKeys.use { keys ->
                    if (!keys.next()) {
                        throw RuntimeException("Failed to get generated ID")
                    }
                    val savedReviewId: Int = keys.getInt(1)
                    findById(savedReviewId) ?: throw NoSuchElementException("Review not found after save")
                }
            }
        )

    override fun deleteById(id: Int) =
        statementExecutor.execute(
            sqlQuery = DELETE_REVIEW_BY_ID,
            configure = { setInt(1, id) },
            process = {
                executeUpdate()
                Unit
            }
        )

    override fun findById(id: Int): Review? =
        statementExecutor.execute(
            sqlQuery = FIND_REVIEW_BY_ID,
            configure = { setInt(1, id) },
            process = { executeQuery().use(reviewMapper::singleOrNull) }
        )

    override fun findByAuthorId(authorId: Int): List<Review> =
        statementExecutor.execute(
            sqlQuery = FIND_REVIEWS_BY_AUTHOR_ID,
            configure = { setInt(1, authorId) },
            process = { executeQuery().use(reviewMapper::list) }
        )

    override fun findByArticleId(articleId: Int): List<Review> =
        statementExecutor.execute(
            sqlQuery = FIND_REVIEWS_BY_ARTICLE_ID,
            configure = { setInt(1, articleId) },
            process = { executeQuery().use(reviewMapper::list) }
        )
}