package pet.articles.aspect.log

import mu.KotlinLogging

import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect

import org.springframework.stereotype.Component

import pet.articles.model.dto.User

@Aspect
@Component
class UserLogAspect {

    private val log = KotlinLogging.logger {}

    @AfterReturning(
        pointcut = "execution(* pet.articles.service.UserServiceImpl.create(..))",
        returning = "createdUser"
    )
    fun logUserCreation(createdUser: User) {
        log.info("User was created successfully {}", createdUser)
    }

    @AfterReturning(
        pointcut = "execution(* pet.articles.service.UserServiceImpl.updateById(..))",
        returning = "updatedUser"
    )
    fun logUserUpdate(updatedUser: User) {
        log.info("User with id = {} was updated successfully {}", updatedUser.id, updatedUser)
    }

    @AfterReturning("execution(* pet.articles.service.UserServiceImpl.deleteById(..)) && args(id)")
    fun logUserDeletion(id: Int) {
        log.info("User with id = {} was deleted successfully", id)
    }
}