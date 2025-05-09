package pet.articles.model.dto.payload;

import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotNull

import pet.articles.model.dto.Review
import pet.articles.model.enums.ReviewType

import java.time.LocalDateTime

object ReviewValidation {

    object FieldConstraints {
        const val CONTENT_MIN_LENGTH = 1
        const val CONTENT_MAX_LENGTH = 500
    }

    object ErrorMessages {
        const val TYPE_NOT_NULL = "review type must not be null"
        const val CONTENT_NOT_NULL = "content must be not null"
        const val CONTENT_SIZE = "the length of the content must be between {min} and {max}"
        const val AUTHOR_ID_POSITIVE = "authorId must be positive"
        const val AUTHOR_ID_NOT_NULL = "authorId must not be null"
        const val ARTICLE_ID_POSITIVE = "articleId must be positive"
        const val ARTICLE_ID_NOT_NULL = "articleId must not be null"
    }
}

data class ReviewPayload(
    @field:NotNull(message = ReviewValidation.ErrorMessages.TYPE_NOT_NULL)
    val type: ReviewType,

    @field:NotNull(message = ReviewValidation.ErrorMessages.CONTENT_NOT_NULL)
    @field:Size(
        min = ReviewValidation.FieldConstraints.CONTENT_MIN_LENGTH,
        max = ReviewValidation.FieldConstraints.CONTENT_MAX_LENGTH,
        message = ReviewValidation.ErrorMessages.CONTENT_SIZE
    )
    val content: String,

    @field:Positive(message = ReviewValidation.ErrorMessages.AUTHOR_ID_POSITIVE)
    @field:NotNull(message = ReviewValidation.ErrorMessages.AUTHOR_ID_NOT_NULL)
    val authorId: Int,

    @field:Positive(message = ReviewValidation.ErrorMessages.ARTICLE_ID_POSITIVE)
    @field:NotNull(message = ReviewValidation.ErrorMessages.ARTICLE_ID_NOT_NULL)
    val articleId: Int
) {
    fun toReview(): Review = Review(
        id = null,
        type = type,
        content = content,
        authorId = authorId,
        articleId = articleId,
        dateOfCreation = LocalDateTime.now()
    )
}