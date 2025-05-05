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

    @Before("execution(* pet.articles.controller.UserController.updateById(..)) && args(userPayload, id)")
    fun secureUserUpdate(userPayload: UserPayload, id: Int) {
        secureEditMethod(id, HttpMethod.PATCH)
    }

    @Before("execution(* pet.articles.controller.UserController.deleteById(..)) && args(id)")
    fun secureUserDeletion(id: Int) {
        secureEditMethod(id, HttpMethod.DELETE)
    }

    private fun secureEditMethod(userId: Int, method: HttpMethod) {
        if (!userPermissionService.checkCurrentUserForEditPermissionById(userId)) {
            throw AccessDeniedException("Attempt to $method user without proper permission")
        }
    }
}
