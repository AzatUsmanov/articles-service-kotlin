package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.Article
import pet.articles.model.dto.payload.UpdateArticlePayload
import pet.articles.test.tool.extension.generateRandom

@Component
class UpdateArticlePayloadTestDataGenerator(
    private val articleTestDataGenerator: TestDataGenerator<Article>
) : TestDataGenerator<UpdateArticlePayload> {

    override fun generateSavedData(): UpdateArticlePayload = TODO()

    override fun generateInvalidData(): UpdateArticlePayload = generateUnsavedData().copy(
        topic = String.generateRandom(1000)
    )

    override fun generateUnsavedData(): UpdateArticlePayload =
        convertToUpdateArticlePayload(articleTestDataGenerator.generateUnsavedData())

    private fun convertToUpdateArticlePayload(article: Article): UpdateArticlePayload =
        UpdateArticlePayload(
            topic = article.topic,
            content = article.content
        )
}