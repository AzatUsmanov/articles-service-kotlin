package pet.articles.tool.db

import org.springframework.stereotype.Component

import pet.articles.model.dto.AuthorshipOfArticle
import pet.articles.model.enums.AuthorshipOfArticleColumn

import java.sql.ResultSet

@Component
class AuthorshipOfArticleMapper : RowMapper<AuthorshipOfArticle> {

    override fun mapRow(rs: ResultSet): AuthorshipOfArticle = AuthorshipOfArticle(
        authorId = rs.getInt(AuthorshipOfArticleColumn.AUTHOR_ID.columnName),
        articleId = rs.getInt(AuthorshipOfArticleColumn.ARTICLE_ID.columnName)
    )
}
