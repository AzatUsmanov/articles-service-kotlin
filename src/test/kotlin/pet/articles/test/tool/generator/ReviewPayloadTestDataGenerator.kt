package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.Review
import pet.articles.model.dto.payload.ReviewPayload
import pet.articles.test.tool.extension.generateRandom

@Component
class ReviewPayloadTestDataGenerator(
    private val reviewTestDataGenerator: TestDataGenerator<Review>
) : TestDataGenerator<ReviewPayload> {

    companion object {
        const val REVIEW_FIELD_TOPIC_INVALID_LENGTH = 1000
    }

    override fun generateSavedData(): ReviewPayload = TODO()

    override fun generateInvalidData(): ReviewPayload = generateUnsavedData().copy(
        content = String.generateRandom(REVIEW_FIELD_TOPIC_INVALID_LENGTH)
    )

    override fun generateUnsavedData(): ReviewPayload =
        convertToReviewPayload(reviewTestDataGenerator.generateUnsavedData())

    private fun convertToReviewPayload(review: Review): ReviewPayload =
        ReviewPayload(
            type = review.type,
            content = review.content,
            authorId = review.authorId!!,
            articleId = review.articleId
        )
}