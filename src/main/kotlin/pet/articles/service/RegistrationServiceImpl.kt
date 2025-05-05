package pet.articles.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import pet.articles.model.dto.User

@Service
class RegistrationServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) : RegistrationService {

    override fun register(user: User): User {
        val userForRegistration: User = buildUserForRegistration(user)
        return userService.create(userForRegistration)
    }

    private fun buildUserForRegistration(user: User): User =
        user.copy(password = passwordEncoder.encode(user.password))
}