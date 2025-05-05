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

    override fun generateSavedData(): Article =
        articleService.create(
            generateUnsavedData(),
            generateSavedAuthorIds((1..5).random())
        )

    override fun generateUnsavedData(): Article =
        Instancio
            .of(Article::class.java)
            .set(Select.field("dateOfCreation"), LocalDateTime.now())
            .generate(Select.field("topic")) { gen ->
                gen.string()
                    .length(TOPIC_MIN_LENGTH, TOPIC_MAX_LENGTH)
                    .alphaNumeric()
            }
            .generate(Select.field("content")) { gen ->
                gen.string()
                    .length(CONTENT_MIN_LENGTH, CONTENT_MAX_LENGTH)
                    .alphaNumeric()
            }
            .create()

    override fun generateInvalidData(): Article = generateUnsavedData().copy(
        topic = String.generateRandom(1000)
    )

    private fun generateSavedAuthorIds(dataSize: Int): List<Int> =
        userTestDataGenerator.generateSavedData(dataSize)
            .map { user -> user.id!!}
}