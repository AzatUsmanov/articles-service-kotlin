package pet.articles.test.tool.generator

import org.instancio.Instancio
import org.instancio.Select

import org.springframework.stereotype.Component

import pet.articles.model.dto.Article
import pet.articles.model.dto.Review
import pet.articles.model.dto.User
import pet.articles.model.dto.payload.ReviewValidation.FieldConstraints.CONTENT_MAX_LENGTH
import pet.articles.model.dto.payload.ReviewValidation.FieldConstraints.CONTENT_MIN_LENGTH
import pet.articles.service.ReviewService
import pet.articles.test.tool.extension.generateRandom

import java.time.LocalDateTime

@Component
class ReviewTestDataGenerator(
    private val reviewService: ReviewService,
    private val userTestDataGenerator: TestDataGenerator<User>,
    private val articleTestDataGenerator: TestDataGenerator<Article>
) : TestDataGenerator<Review> {

    override fun generateSavedData(): Review = reviewService.create(generateUnsavedData())

    override fun generateInvalidData(): Review = generateUnsavedData().copy(
        content = String.generateRandom(1000)
    )

    override fun generateUnsavedData(): Review =
        Instancio
            .of(Review::class.java)
            .set(Select.field("dateOfCreation"), LocalDateTime.now())
            .set(Select.field("authorId"), userTestDataGenerator.generateSavedData().id!!)
            .set(Select.field("articleId"), articleTestDataGenerator.generateSavedData().id!!)
            .generate(Select.field("content")) { gen ->
                gen.string()
                    .length(CONTENT_MIN_LENGTH, CONTENT_MAX_LENGTH)
                    .alphaNumeric()
            }
            .create()
}