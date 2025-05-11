package pet.articles.test.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import pet.articles.model.dto.Article
import pet.articles.model.dto.Review
import pet.articles.model.dto.User
import pet.articles.service.ArticleService
import pet.articles.service.ReviewService
import pet.articles.service.UserService
import pet.articles.test.tool.db.DBCleaner
import pet.articles.test.tool.generator.TestDataGenerator

@SpringBootTest
class ReviewServiceTest {

    companion object {
        const val NUM_OF_TEST_REVIEWS = 10
    }

    @Autowired
    private lateinit var dbCleaner: DBCleaner

    @Autowired
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var articleService: ArticleService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userTestDataGenerator: TestDataGenerator<User>

    @Autowired
    private lateinit var articleTestDataGenerator: TestDataGenerator<Article>

    @Autowired
    private lateinit var reviewTestDataGenerator: TestDataGenerator<Review>

    @AfterEach
    fun cleanDb() {
        dbCleaner.cleanUp()
    }

    @Test
    fun createReview() {
        val reviewForSave: Review = reviewTestDataGenerator.generateUnsavedData()

        val savedReview: Review = reviewService.create(reviewForSave)

        val reviewForCheck: Review? = reviewService.findById(savedReview.id!!)
        assertNotNull(reviewForCheck)
        assertEquals(savedReview, reviewForCheck)
    }
    
    @Test
    fun createReviewWithNonExistentArticleId() {
        val unsavedArticle: Article = articleTestDataGenerator.generateUnsavedData()
        val reviewForSave: Review = reviewTestDataGenerator.generateUnsavedData().copy(
            articleId = unsavedArticle.id!!
        )
        
        assertThrows(RuntimeException::class.java) {
            reviewService.create(reviewForSave)
        }
    }

    @Test
    fun createReviewWithNonExistentAuthorId() {
        val unsavedAuthor: User = userTestDataGenerator.generateUnsavedData()
        val reviewForSave: Review = reviewTestDataGenerator.generateUnsavedData().copy(
            authorId = unsavedAuthor.id!!
        )

        assertThrows(RuntimeException::class.java) {
            reviewService.create(reviewForSave)
        }
    }

    @Test
    fun deleteReviewById() {
        val savedReview: Review = reviewTestDataGenerator.generateSavedData()

        reviewService.deleteById(savedReview.id!!)

        val reviewForCheck: Review? = reviewService.findById(savedReview.id!!)
        assertNull(reviewForCheck)
    }

    @Test
    fun deleteReviewByNonExistentId() {
        val unsavedReview: Review = reviewTestDataGenerator.generateUnsavedData()

        reviewService.deleteById(unsavedReview.id!!)

        val reviewForCheck: Review? = reviewService.findById(unsavedReview.id!!)
        assertNull(reviewForCheck)
    }

    @Test
    fun deleteReviewWhenDeletingArticle() {
        val savedReview: Review = reviewTestDataGenerator.generateSavedData()
        val article: Article = articleService.findById(savedReview.articleId)!!

        articleService.deleteById(article.id!!)

        val reviewForCheck: Review? = reviewService.findById(savedReview.id!!)
        assertNull(reviewForCheck)
    }

    @Test
    fun deleteReviewWhenDeletingAuthor() {
        val savedReview: Review = reviewTestDataGenerator.generateSavedData()
        val author: User = userService.findById(savedReview.authorId!!)!!

        userService.deleteById(author.id!!)

        val reviewForCheck: Review? = reviewService.findById(savedReview.id!!)
        assertNotNull(reviewForCheck)
        assertNull(reviewForCheck!!.authorId)
    }

    @Test
    fun findReviewById() {
        val savedReview: Review = reviewTestDataGenerator.generateSavedData()

        val reviewForCheck: Review? = reviewService.findById(savedReview.id!!)

        assertNotNull(reviewForCheck)
        assertEquals(savedReview, reviewForCheck)
    }

    @Test
    fun findReviewByNonExistentId() {
        val unsavedReview: Review = reviewTestDataGenerator.generateUnsavedData()

        val reviewForCheck: Review? = reviewService.findById(unsavedReview.id!!)

        assertNull(reviewForCheck)
    }

    @Test
    fun findReviewsByAuthorId() {
        val reviews: List<Review> = reviewTestDataGenerator.generateSavedData(NUM_OF_TEST_REVIEWS)
        val authorId: Int = reviews.random().authorId!!
        val allReviewsByAuthor: List<Review> = reviews.filter { it.authorId == authorId }

        val reviewsForCheck: List<Review> = reviewService.findByAuthorId(authorId)

        assertEquals(allReviewsByAuthor.size, reviewsForCheck.size)
        assertTrue(allReviewsByAuthor.containsAll(reviewsForCheck))
        assertTrue(reviewsForCheck.containsAll(allReviewsByAuthor))
    }

    @Test
    fun findReviewsByNonExistentAuthorId() {
        val unsavedAuthor: User = userTestDataGenerator.generateUnsavedData()

        val reviews: List<Review> = reviewService.findByAuthorId(unsavedAuthor.id!!)

        assertTrue(reviews.isEmpty())
    }

    @Test
    fun findReviewsByArticleId() {
        val reviews: List<Review> = reviewTestDataGenerator.generateSavedData(NUM_OF_TEST_REVIEWS)
        val articleId: Int = reviews.random().articleId
        val allReviewsForArticle: List<Review> = reviews.filter { it.articleId == articleId }

        val reviewsForCheck: List<Review> = reviewService.findByArticleId(articleId)

        assertEquals(allReviewsForArticle.size, reviewsForCheck.size)
        assertTrue(allReviewsForArticle.containsAll(reviewsForCheck))
        assertTrue(reviewsForCheck.containsAll(allReviewsForArticle))
    }

    @Test
    fun findReviewsByNonExistentArticleId() {
        val unsavedArticle: Article = articleTestDataGenerator.generateUnsavedData()

        val reviews: List<Review> = reviewService.findByArticleId(unsavedArticle.id!!)

        assertTrue(reviews.isEmpty())
    }
}