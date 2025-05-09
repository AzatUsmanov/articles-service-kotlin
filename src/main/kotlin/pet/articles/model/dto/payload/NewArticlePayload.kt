package pet.articles.model.dto.payload

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import pet.articles.model.dto.Article

import java.time.LocalDateTime

object ArticleValidation {

    object FieldConstraints {
        const val TOPIC_MIN_LENGTH = 1
        const val TOPIC_MAX_LENGTH = 50
        const val CONTENT_MIN_LENGTH = 1
        const val CONTENT_MAX_LENGTH = 1500
    }

    object ErrorMessages {
        const val TOPIC_NOT_NULL = "topic must be not null"
        const val TOPIC_SIZE = "the length of the topic must be between {min} and {max}"
        const val CONTENT_NOT_NULL = "content must be not null"
        const val CONTENT_SIZE = "the length of the content must be between {min} and {max}"
        const val AUTHOR_IDS_NOT_EMPTY = "authorIds must be not empty"
        const val AUTHOR_IDS_NOT_NULL = "username must be not null"
    }
}

data class NewArticlePayload(
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
    val content: String,

    @field:NotEmpty(message = ArticleValidation.ErrorMessages.AUTHOR_IDS_NOT_EMPTY)
    @field:NotNull(message = ArticleValidation.ErrorMessages.AUTHOR_IDS_NOT_NULL)
    val authorIds: List<Int>
) {
    fun toArticle(): Article = Article(
        id = null,
        topic = topic,
        content = content,
        dateOfCreation = LocalDateTime.now()
    )
}

