package pet.articles.tool.converter;

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import pet.articles.model.dto.User

@Component
class UserToUserDetailsConverter : Converter<User, UserDetails> {
    override fun convert(source: User): UserDetails = org.springframework.security.core.userdetails.User(
        source.username,
        source.password,
        listOf(
            SimpleGrantedAuthority(source.role.toString())
        )
    )
}