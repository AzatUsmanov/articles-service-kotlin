package pet.articles.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import pet.articles.model.dto.User
import pet.articles.service.UserService
import pet.articles.tool.converter.UserToUserDetailsConverter

@Service
class UserDetailsServiceImpl(
    private val userService: UserService,
    private val userToUserDetailsConverter: UserToUserDetailsConverter
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userService.findByUsername(username)
            ?: throw UsernameNotFoundException("User with username $username not found")
        return userToUserDetailsConverter.convert(user)
    }
}