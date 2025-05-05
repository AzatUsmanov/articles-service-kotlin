package pet.articles.model.dto.payload

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import pet.articles.model.dto.Article

import java.time.LocalDateTime

data class UpdateArticlePayload(
    @field:NotNull(message = ArticleValidation.ErrorMessages.TOPIC_NOT_NULL)
    @field:Size(
        min = ArticleValidation.FieldConstraints.TOPIC_MIN_LENGTH,
        max = ArticleValidation.FieldConstraints.TOPIC_MAX_LENGTH,
        message = ArticleValidation.ErrorMessages.TOPIC_SIZE
    )
    val topic: String,

    @field:NotNull(message = ArticleValidation.ErrorMessages.CONTENT_NOT_NULL)
    @field:Size(
        min = ArticleValidation.FieldConstraints.CONTENT_MIN_LENGTH,
        max = ArticleValidation.FieldConstraints.CONTENT_MAX_LENGTH,
        message = ArticleValidation.ErrorMessages.CONTENT_SIZE
    )
    val content: String
) {
    fun toArticle(): Article = Article(
        id = null,
        topic = topic,
        content = content,
        dateOfCreation = LocalDateTime.now()
    )
}