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

    override fun generateSavedData(): NewArticlePayload = TODO()

    override fun generateInvalidData(): NewArticlePayload = generateUnsavedData().copy(
        topic = String.generateRandom(1000)
    )

    override fun generateUnsavedData(): NewArticlePayload =
        convertToNewArticlePayload(
            articleTestDataGenerator.generateUnsavedData(),
            generateSavedAuthorIds((1..5).random())
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