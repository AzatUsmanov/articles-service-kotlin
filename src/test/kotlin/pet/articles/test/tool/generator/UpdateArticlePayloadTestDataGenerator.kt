package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.Article
import pet.articles.model.dto.payload.UpdateArticlePayload

@Component
class UpdateArticlePayloadTestDataGenerator(
    private val articleTestDataGenerator: TestDataGenerator<Article>
) : TestDataGenerator<UpdateArticlePayload> {

    override fun generateSavedData(dataSize: Int): List<UpdateArticlePayload> =
        articleTestDataGenerator.generateSavedData(dataSize).map(::convertToUpdateArticlePayload)

    override fun generateUnsavedData(dataSize: Int): List<UpdateArticlePayload> =
        articleTestDataGenerator.generateUnsavedData(dataSize).map(::convertToUpdateArticlePayload)

    override fun generateInvalidData(): UpdateArticlePayload =
        convertToUpdateArticlePayload(articleTestDataGenerator.generateInvalidData())

    private fun convertToUpdateArticlePayload(article: Article): UpdateArticlePayload =
        UpdateArticlePayload(
            topic = article.topic,
            content = article.content
        )
}