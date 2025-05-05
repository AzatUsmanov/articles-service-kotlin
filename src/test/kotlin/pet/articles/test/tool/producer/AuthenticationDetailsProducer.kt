package pet.articles.test.tool.producer

import org.springframework.security.core.userdetails.UserDetails

import pet.articles.model.dto.User
import pet.articles.model.enums.UserRole

interface AuthenticationDetailsProducer {
    fun produceUserDetailsOfRegisteredUser(role: UserRole): UserDetails

    fun produceRegisteredUserWithRawPassword(role: UserRole): User
}