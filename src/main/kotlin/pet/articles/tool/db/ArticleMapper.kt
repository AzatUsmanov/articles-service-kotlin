package pet.articles.tool.db

import org.springframework.stereotype.Component

import pet.articles.model.dto.Article
import pet.articles.model.enums.ArticleColumn

import java.sql.ResultSet

@Component
class ArticleMapper : RowMapper<Article> {
    override fun mapRow(rs: ResultSet): Article = Article(
        id = rs.getInt(ArticleColumn.ID.columnName),
        topic = rs.getString(ArticleColumn.TOPIC.columnName),
        content = rs.getString(ArticleColumn.CONTENT.columnName),
        dateOfCreation = rs.getTimestamp(ArticleColumn.DATE_OF_CREATION.columnName)
            .toLocalDateTime()
    )
}