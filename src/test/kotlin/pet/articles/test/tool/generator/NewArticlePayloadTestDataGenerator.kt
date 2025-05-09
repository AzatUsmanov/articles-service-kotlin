package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.Article
import pet.articles.model.dto.User
import pet.articles.model.dto.payload.NewArticlePayload
import pet.articles.test.tool.extension.generateRandom

@Component
class NewArticlePayloadTestDataGenerator(
    private val articleTestDataGenerator: TestDataGenerator<Article>,
    private val userTestDataGenerator: TestDataGenerator<User>
) : TestDataGenerator<NewArticlePayload> {

    companion object {
        const val MIN_NUM_OF_TEST_AUTHOR_IDS = 1
        const val MAX_NUM_OF_TEST_AUTHOR_IDS = 5

        const val ARTICLE_FIELD_TOPIC_INVALID_LENGTH = 1000
    }

    override fun generateSavedData(): NewArticlePayload = TODO()

    override fun generateInvalidData(): NewArticlePayload = generateUnsavedData().copy(
        topic = String.generateRandom(ARTICLE_FIELD_TOPIC_INVALID_LENGTH)
    )

    override fun generateUnsavedData(): NewArticlePayload =
        convertToNewArticlePayload(
            articleTestDataGenerator.generateUnsavedData(),
            generateSavedAuthorIds(
                (MIN_NUM_OF_TEST_AUTHOR_IDS..MAX_NUM_OF_TEST_AUTHOR_IDS).random()
            )
        )

    private fun convertToNewArticlePayload(
        article: Article,
        savedAuthorIds: List<Int>
    ): NewArticlePayload = NewArticlePayload(
        topic = article.topic,
        content = article.content,
        authorIds = savedAuthorIds
    )

    private fun generateSavedAuthorIds(dataSize: Int): List<Int> =
        userTestDataGenerator.generateSavedData(dataSize)
            .map{ user -> user.id!! }
            .toMutableList()
}