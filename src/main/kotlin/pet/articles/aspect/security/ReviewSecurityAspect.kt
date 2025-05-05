package pet.articles.aspect.security;

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.http.HttpMethod
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import pet.articles.model.dto.payload.ReviewPayload
import pet.articles.service.ReviewService
import pet.articles.service.UserPermissionService

@Aspect
@Component
class ReviewSecurityAspect(
    private val userPermissionService: UserPermissionService,
    private val reviewService: ReviewService
) {

    @Before("execution(* pet.articles.controller.ReviewController.create(..)) && args(reviewPayload)")
    fun secureReviewCreation(reviewPayload: ReviewPayload) {
        secureEditMethod(reviewPayload.authorId, HttpMethod.POST)
    }

    @Before("execution(* pet.articles.controller.ReviewController.deleteById(..)) && args(id)")
    fun secureReviewDeletion(id: Int) {
        reviewService.findById(id)?.let { review ->
            secureEditMethod(review.authorId, HttpMethod.DELETE)
        }
    }

    private fun secureEditMethod(userId: Int, method: HttpMethod) {
        if (!userPermissionService.checkUserForEditPermissionById(userId)) {
            throw AccessDeniedException("Attempt to $method user without proper permission")
        }
    }
}

