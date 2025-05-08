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
import pet.articles.service.UserService
import pet.articles.test.tool.db.DBCleaner
import pet.articles.test.tool.generator.TestDataGenerator

import java.sql.SQLException
import java.util.NoSuchElementException

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private lateinit var dbCleaner: DBCleaner

    @Autowired
    private lateinit var articleService: ArticleService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userTestDataGenerator: TestDataGenerator<User>

    @Autowired
    private lateinit var articleTestDataGenerator: TestDataGenerator<Article>

    @AfterEach
    fun cleanDb() {
        dbCleaner.cleanUp()
    }

    @Test
    fun createArticle() {
        val articleForSave: Article = articleTestDataGenerator.generateUnsavedData()
        val savedAuthors: List<User> = userTestDataGenerator.generateSavedData(10)
        val authorIds: List<Int> = savedAuthors.map { it.id!! }

        val savedArticle: Article = articleService.create(articleForSave, authorIds)

        val articleForCheck: Article? = articleService.findById(savedArticle.id!!)
        val authorsForCheck: List<User> = userService.findAuthorsByArticleId(savedArticle.id!!)
        assertNotNull(articleForCheck)
        assertEquals(savedArticle, articleForCheck)
        assertEquals(savedAuthors.size, authorsForCheck.size)
        assertTrue(savedAuthors.containsAll(authorsForCheck))
        assertTrue(authorsForCheck.containsAll(savedAuthors))
    }

    @Test
    fun createArticleWithoutAuthors() {
        val articleForSave: Article = articleTestDataGenerator.generateUnsavedData()

        val savedArticle: Article = articleService.create(articleForSave, emptyList())

        val articleForCheck: Article? = articleService.findById(savedArticle.id!!)
        assertNotNull(articleForCheck)
        assertEquals(savedArticle, articleForCheck)
    }

    @Test
    fun saveArticleWithInvalidData() {
        val invalidArticle: Article = articleTestDataGenerator.generateInvalidData()

        assertThrows(SQLException::class.java) {
            articleService.create(invalidArticle, emptyList())
        }
    }

    @Test
    fun updateArticleById() {
        val savedArticle: Article = articleTestDataGenerator.generateSavedData()
        val articleDataForUpdate: Article = articleTestDataGenerator.generateUnsavedData()

        val updatedArticle: Article = articleService.updateById(articleDataForUpdate, savedArticle.id!!)

        val articleForCheck: Article? = articleService.findById(savedArticle.id!!)
        assertNotNull(articleForCheck)
        assertEquals(updatedArticle, articleForCheck)
    }

    @Test
    fun updateArticleByIdWithInvalidData() {
        val savedArticle: Article = articleTestDataGenerator.generateSavedData()
        val invalidArticle: Article = articleTestDataGenerator.generateInvalidData()

        assertThrows(SQLException::class.java) {
            articleService.updateById(invalidArticle, savedArticle.id!!)
        }
    }

    @Test
    fun updateArticleByNonExistentId() {
        val articleDataForUpdate: Article = articleTestDataGenerator.generateUnsavedData()

        assertThrows(NoSuchElementException::class.java) {
            articleService.updateById(articleDataForUpdate, articleDataForUpdate.id!!)
        }
    }

    @Test
    fun deleteArticleById() {
        val savedArticle: Article = articleTestDataGenerator.generateSavedData()

        articleService.deleteById(savedArticle.id!!)

        val articleForCheck: Article? = articleService.findById(savedArticle.id!!)
        assertNull(articleForCheck)
    }

    @Test
    fun deleteArticleNonExistentId() {
        val unsavedArticle: Article = articleTestDataGenerator.generateUnsavedData()

        articleService.deleteById(unsavedArticle.id!!)

        val articleForCheck: Article? = articleService.findById(unsavedArticle.id!!)
        assertNull(articleForCheck)
    }

    @Test
    fun findArticleById() {
        val savedArticle: Article = articleTestDataGenerator.generateSavedData()

        val articleForCheck: Article? = articleService.findById(savedArticle.id!!)

        assertNotNull(articleForCheck)
        assertEquals(savedArticle, articleForCheck)
    }

    @Test
    fun findArticleByNonExistentId() {
        val unsavedArticle: Article = articleTestDataGenerator.generateUnsavedData()

        val articleForCheck: Article? = articleService.findById(unsavedArticle.id!!)

        assertNull(articleForCheck)
    }

    @Test
    fun findArticlesByAuthorId() {
        val savedAuthor: User = userTestDataGenerator.generateSavedData()
        val unsavedArticles: List<Article> = articleTestDataGenerator.generateUnsavedData(10)
        val savedArticles: List<Article> = unsavedArticles.map {
            articleService.create(it, listOf(savedAuthor.id!!))
        }

        val articlesForCheck: List<Article> = articleService.findArticlesByAuthorId(savedAuthor.id!!)
        assertEquals(savedArticles.size, articlesForCheck.size)
        assertTrue(articlesForCheck.containsAll(savedArticles))
        assertTrue(savedArticles.containsAll(articlesForCheck))
    }

    @Test
    fun findArticlesByNonExistentAuthorId() {
        val unsavedAuthor: User = userTestDataGenerator.generateUnsavedData()

        val articles: List<Article> = articleService.findArticlesByAuthorId(unsavedAuthor.id!!)

        assertTrue(articles.isEmpty())
    }

    @Test
    fun findAllArticles() {
        val allArticles: List<Article> = articleTestDataGenerator.generateSavedData(10)

        val articlesForCheck: List<Article> = articleService.findAll()

        assertEquals(allArticles.size, articlesForCheck.size)
        assertTrue(allArticles.containsAll(articlesForCheck))
        assertTrue(articlesForCheck.containsAll(allArticles))
    }
}