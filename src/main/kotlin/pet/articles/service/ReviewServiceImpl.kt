package pet.articles.service

import org.springframework.stereotype.Service

import pet.articles.model.dto.Review
import pet.articles.repository.ReviewRepository

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val userService: UserService,
    private val articleService: ArticleService
) : ReviewService {

    override fun create(review: Review): Review = reviewRepository.save(review)

    override fun deleteById(id: Int) = reviewRepository.deleteById(id)

    override fun findById(id: Int): Review? = reviewRepository.findById(id)

    //переписать без исключения
    override fun findByAuthorId(authorId: Int): List<Review> {
        if (!userService.existsById(authorId)) {
            throw NoSuchElementException("Attempt to find reviews by non existent author id = $authorId")
        }
        return reviewRepository.findByAuthorId(authorId)
    }

    override fun findByArticleId(articleId: Int): List<Review> {
        if (!articleService.existsById(articleId)) {
            throw NoSuchElementException("Attempt to find reviews by non existent article id = $articleId")
        }
        return reviewRepository.findByArticleId(articleId)
    }
}