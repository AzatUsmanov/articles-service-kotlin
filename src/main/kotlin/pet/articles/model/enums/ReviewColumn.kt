package pet.articles.model.enums

enum class ReviewColumn(val columnName : String) {
    ID("id"),
    TYPE("type"),
    DATE_OF_CREATION("date_of_creation"),
    CONTENT("content"),
    AUTHOR_ID("author_id"),
    ARTICLE_ID("article_id")
}
