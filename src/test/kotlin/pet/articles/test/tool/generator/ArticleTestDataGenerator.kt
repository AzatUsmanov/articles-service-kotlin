package pet.articles.test.tool.generator

import net.datafaker.Faker

import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.service.ArticleService
import pet.articles.model.dto.Article
import pet.articles.model.dto.payload.ArticleValidation.FieldConstraints.CONTENT_MAX_LENGTH
import pet.articles.model.dto.payload.ArticleValidation.FieldConstraints.TOPIC_MAX_LENGTH
import pet.articles.test.tool.extension.generateRandom

import java.time.LocalDateTime

@Component
class ArticleTestDataGenerator(
    private val faker: Faker = Faker(),
    private val articleService: ArticleService,
    private val userTestDataGenerator: TestDataGenerator<User>
) : TestDataGenerator<Article> {

    companion object {
        const val ARTICLE_FIELD_TOPIC_INVALID_LENGTH = 1000

        const val CONTENT_PARAGRAPH_SIZE = 3
        const val CONTENT_SENTENCE_SIZE = 3

        private const val MIN_NUM_OF_TEST_AUTHOR_IDS = 1
        private const val MAX_NUM_OF_TEST_AUTHOR_IDS = 5

        fun generateSavedAuthorIds(userTestDataGenerator: TestDataGenerator<User>): List<Int> =
            userTestDataGenerator.generateSavedData(generateRandomSizeOfAuthorIdsList())
                .map{ user -> user.id!! }
                .toMutableList()

        private fun generateRandomSizeOfAuthorIdsList() =
            (MIN_NUM_OF_TEST_AUTHOR_IDS..MAX_NUM_OF_TEST_AUTHOR_IDS).random()
    }

    override fun generateUnsavedData(dataSize: Int): List<Article> =
        (1..dataSize).map {
            Article(
                id = faker.number().positive(),
                dateOfCreation = LocalDateTime.now(),
                topic = faker.lorem().sentence(CONTENT_SENTENCE_SIZE).take(TOPIC_MAX_LENGTH),
                content = faker.lorem().paragraph(CONTENT_PARAGRAPH_SIZE).take(CONTENT_MAX_LENGTH)
            )
        }

    override fun generateSavedData(dataSize: Int): List<Article> =
        generateUnsavedData(dataSize).map { article ->
            articleService.create(article, generateSavedAuthorIds(userTestDataGenerator))
        }

    override fun generateInvalidData(): Article = generateUnsavedData().copy(
        topic = String.generateRandom(ARTICLE_FIELD_TOPIC_INVALID_LENGTH)
    )
}
