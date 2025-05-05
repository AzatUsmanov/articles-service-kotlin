package pet.articles.service

interface UserPermissionService {
    fun checkUserForEditPermissionById(id: Int): Boolean
}