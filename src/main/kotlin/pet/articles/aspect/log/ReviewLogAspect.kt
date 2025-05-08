package pet.articles.aspect.log

import mu.KotlinLogging

import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect

import org.springframework.stereotype.Component

import pet.articles.model.dto.Review

@Aspect
@Component
class ReviewLogAspect {

    private val log = KotlinLogging.logger {}

    @AfterReturning(
        pointcut = "execution(* pet.articles.service.ReviewServiceImpl.create(..))",
        returning = "createdReview"
    )
    fun logReviewCreation(createdReview: Review) {
        log.info { "Review was created successfully $createdReview" }
    }

    @AfterReturning("execution(* pet.articles.service.ReviewServiceImpl.deleteById(..)) && args(id)")
    fun logReviewDeletion(id: Int) {
        log.info { "Review with id = $id was deleted successfully" }
    }
}