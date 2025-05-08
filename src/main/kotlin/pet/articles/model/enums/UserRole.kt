package pet.articles.model.enums

import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class UserRole(val roleName: String) {

    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    fun toGrantedAuthority() = SimpleGrantedAuthority(toString())
}
