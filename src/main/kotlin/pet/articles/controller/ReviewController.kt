package pet.articles.controller

import jakarta.validation.Valid

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import pet.articles.model.dto.Review
import pet.articles.model.dto.payload.ReviewPayload
import pet.articles.service.ReviewService

@RestController
@RequestMapping(
    path = ["\${api.paths.reviews}"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class ReviewController(
    private val reviewService: ReviewService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid reviewPayload: ReviewPayload): Review =
        reviewService.create(reviewPayload.toReview())

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) = reviewService.deleteById(id)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<Review> =
        reviewService.findById(id)?.let { review -> ResponseEntity.ok(review) }
            ?: ResponseEntity.notFound().build()

    @GetMapping("/articles/{articleId}")
    fun findByArticleId(@PathVariable articleId: Int): List<Review> =
        reviewService.findByArticleId(articleId)

    @GetMapping("/users/{authorId}")
    fun findByAuthorId(@PathVariable authorId: Int): List<Review> =
        reviewService.findByAuthorId(authorId)
}
