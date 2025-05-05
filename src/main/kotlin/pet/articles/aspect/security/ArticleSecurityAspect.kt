package pet.articles.aspect.security

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

import org.springframework.http.HttpMethod
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

import pet.articles.model.dto.payload.NewArticlePayload
import pet.articles.model.dto.payload.UpdateArticlePayload

import pet.articles.service.UserPermissionService
import pet.articles.service.UserService

@Aspect
@Component
class ArticleSecurityAspect(
    private val userPermissionService: UserPermissionService,
    private val userService: UserService
) {

    @Before("execution(* pet.articles.controller.ArticleController.create(..)) && args(articlePayload)")
    fun secureArticleCreation(articlePayload: NewArticlePayload) {
        secureEditMethod(articlePayload.authorIds, HttpMethod.POST)
    }

    @Before("execution(* pet.articles.controller.ArticleController.updateById(..)) && args(articlePayload, id)")
    fun secureArticleUpdate(articlePayload: UpdateArticlePayload, id: Int) {
        secureEditMethod(userService.findAuthorIdsByArticleId(id), HttpMethod.PATCH)
    }

    @Before("execution(* pet.articles.controller.ArticleController.deleteById(..)) && args(id)")
    fun secureArticleDeletion(id: Int) {
        secureEditMethod(userService.findAuthorIdsByArticleId(id), HttpMethod.DELETE)
    }

    private fun secureEditMethod(authorIds: List<Int>, method: HttpMethod) {
        if (!userPermissionService.checkCurrentUserIsAdmin()
            && authorIds.none(userPermissionService::checkCurrentUserForEditPermissionById)) {
            throw AccessDeniedException("Attempt to $method article without proper permission")
        }
    }
}