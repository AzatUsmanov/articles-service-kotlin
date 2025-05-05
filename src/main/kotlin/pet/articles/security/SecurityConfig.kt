package pet.articles.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

import pet.articles.model.enums.UserRole

const val SWAGGER_UI_PATH = "/swagger-ui/**"
const val SWAGGER_RESOURCES_PATH = "/swagger-resources/*"
const val API_DOCS = "/v3/api-docs/**"

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${api.paths.users}") private val usersPath: String,
    @Value("\${api.paths.articles}") private val articlesPath: String,
    @Value("\${api.paths.reviews}") private val reviewsPath: String,
    @Value("\${api.paths.registration}") private val registrationPath: String,
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .authorizeHttpRequests {
            it
                .requestMatchers(registrationPath, SWAGGER_UI_PATH, SWAGGER_RESOURCES_PATH, API_DOCS).permitAll()
                .requestMatchers(HttpMethod.POST, usersPath).hasRole(UserRole.ROLE_ADMIN.roleName)
                .requestMatchers(usersPath, articlesPath, reviewsPath).hasAnyRole(
                    UserRole.ROLE_USER.roleName, UserRole.ROLE_ADMIN.roleName
                )
                .anyRequest().authenticated()
            // additional security checks are performed in aspect at the controller level
        }
        .build()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService)
        daoAuthenticationProvider.setUserDetailsService(userDetailsService)
        return daoAuthenticationProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}