package pet.articles.model.dto

import java.time.LocalDateTime

data class Article(
    val id : Int?,
    val dateOfCreation : LocalDateTime,
    val topic : String,
    val content : String
)

