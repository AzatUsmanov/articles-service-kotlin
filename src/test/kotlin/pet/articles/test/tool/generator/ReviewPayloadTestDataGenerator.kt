package pet.articles.test.tool.generator

import org.springframework.stereotype.Component

import pet.articles.model.dto.Review
import pet.articles.model.dto.payload.ReviewPayload

@Component
class ReviewPayloadTestDataGenerator(
    private val reviewTestDataGenerator: TestDataGenerator<Review>
) : TestDataGenerator<ReviewPayload> {

    override fun generateUnsavedData(dataSize: Int): List<ReviewPayload> =
        reviewTestDataGenerator.generateUnsavedData(dataSize).map(::convertToReviewPayload)

    override fun generateSavedData(dataSize: Int): List<ReviewPayload> =
        reviewTestDataGenerator.generateSavedData(dataSize).map(::convertToReviewPayload)

    override fun generateInvalidData(): ReviewPayload =
        convertToReviewPayload(reviewTestDataGenerator.generateInvalidData())

    private fun convertToReviewPayload(review: Review): ReviewPayload =
        ReviewPayload(
            type = review.type,
            content = review.content,
            authorId = review.authorId!!,
            articleId = review.articleId
        )
}