package pet.articles.model.dto

import pet.articles.model.enums.ReviewType

import java.time.LocalDateTime

data class Review(
    val id : Int?,
    val type : ReviewType,
    val dateOfCreation : LocalDateTime,
    val content : String,
    val authorId : Int,
    val articleId : Int
)
