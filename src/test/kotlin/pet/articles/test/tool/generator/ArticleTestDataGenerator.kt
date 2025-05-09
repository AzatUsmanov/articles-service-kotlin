package pet.articles.test.tool.generator

import org.instancio.Instancio
import org.instancio.Select

import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.service.ArticleService
import pet.articles.model.dto.Article
import pet.articles.model.dto.payload.ArticleValidation.FieldConstraints.CONTENT_MAX_LENGTH
import pet.articles.model.dto.payload.ArticleValidation.FieldConstraints.CONTENT_MIN_LENGTH
import pet.articles.model.dto.payload.ArticleValidation.FieldConstraints.TOPIC_MAX_LENGTH
import pet.articles.model.dto.payload.ArticleValidation.FieldConstraints.TOPIC_MIN_LENGTH
import pet.articles.test.tool.extension.generateRandom

import java.time.LocalDateTime

@Component
class ArticleTestDataGenerator(
    private val userTestDataGenerator: TestDataGenerator<User>,
    private val articleService: ArticleService
) : TestDataGenerator<Article> {

    companion object {
        const val MIN_NUM_OF_TEST_AUTHOR_IDS = 1
        const val MAX_NUM_OF_TEST_AUTHOR_IDS = 5

        const val ARTICLE_FIELD_DATE_OF_CREATION_NAME = "dateOfCreation"
        const val ARTICLE_FIELD_TOPIC_NAME = "topic"
        const val ARTICLE_FIELD_CONTENT_NAME = "content"

        const val ARTICLE_FIELD_TOPIC_INVALID_LENGTH= 1000
    }

    override fun generateSavedData(): Article =
        articleService.create(
            generateUnsavedData(),
            generateSavedAuthorIds(
                (MIN_NUM_OF_TEST_AUTHOR_IDS..MAX_NUM_OF_TEST_AUTHOR_IDS).random()
            )
        )

    override fun generateUnsavedData(): Article =
        Instancio
            .of(Article::class.java)
            .set(Select.field(ARTICLE_FIELD_DATE_OF_CREATION_NAME), LocalDateTime.now())
            .generate(Select.field(ARTICLE_FIELD_TOPIC_NAME)) { gen ->
                gen.string()
                    .length(TOPIC_MIN_LENGTH, TOPIC_MAX_LENGTH)
                    .alphaNumeric()
            }
            .generate(Select.field(ARTICLE_FIELD_CONTENT_NAME)) { gen ->
                gen.string()
                    .length(CONTENT_MIN_LENGTH, CONTENT_MAX_LENGTH)
                    .alphaNumeric()
            }
            .create()

    override fun generateInvalidData(): Article = generateUnsavedData().copy(
        topic = String.generateRandom(ARTICLE_FIELD_TOPIC_INVALID_LENGTH)
    )

    private fun generateSavedAuthorIds(dataSize: Int): List<Int> =
        userTestDataGenerator.generateSavedData(dataSize)
            .map { user -> user.id!!}
}