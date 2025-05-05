package pet.articles.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

import pet.articles.model.dto.User
import pet.articles.model.enums.UserRole

@Service
class UserPermissionServiceImpl(
    private val userService: UserService
) : UserPermissionService {

    override fun checkUserForEditPermissionById(id: Int): Boolean {
        val userRoleAuthority = SimpleGrantedAuthority(UserRole.ROLE_USER.toString())
        val currentUser: UserDetails = getCurrentUser()

        if (currentUser.authorities.contains(userRoleAuthority)) {
            val targetUser: User = getTargetUserById(id)
            return isCurrentUserMatchesTarget(currentUser, targetUser)
        }
        return true
    }

    private fun getTargetUserById(id: Int): User = userService.findById(id)
        ?: throw NoSuchElementException("User with id $id not found")

    private fun getCurrentUser(): UserDetails =
        SecurityContextHolder.getContext().authentication.principal as UserDetails

    private fun isCurrentUserMatchesTarget(currentUser: UserDetails, targetUser: User): Boolean =
        currentUser.username == targetUser.username
}