package pet.articles.aspect.security

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

import org.springframework.http.HttpMethod
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

import pet.articles.model.dto.payload.UserPayload
import pet.articles.service.UserPermissionService

@Aspect
@Component
class UserSecurityAspect(
    private val userPermissionService: UserPermissionService
) {

    @Before("execution(* pet.articles.controller.UserController.deleteById(..)) && args(id)")
    fun secureUserDeletion(id: Int) {
        if (!userPermissionService.checkCurrentUserForEditPermissionById(id)) {
            throw AccessDeniedException("Attempt to delete user without proper permission")
        }
    }
}
