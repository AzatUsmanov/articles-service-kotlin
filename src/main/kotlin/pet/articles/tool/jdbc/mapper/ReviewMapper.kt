package pet.articles.tool.jdbc.mapper

import org.springframework.stereotype.Component

import pet.articles.model.dto.Review
import pet.articles.model.enums.ReviewColumn
import pet.articles.model.enums.ReviewType

import java.sql.ResultSet

@Component
class ReviewMapper : RowMapper<Review> {

    private companion object {
        const val ZERO_VALUE = 0
    }

    override fun mapRow(rs: ResultSet): Review = Review(
        id = rs.getInt(ReviewColumn.ID.columnName),
        content = rs.getString(ReviewColumn.CONTENT.columnName),
        authorId = rs.getInt(ReviewColumn.AUTHOR_ID.columnName).takeIf(::checkIdValidation),
        articleId = rs.getInt(ReviewColumn.ARTICLE_ID.columnName),
        type = ReviewType.entries[rs.getInt(ReviewColumn.TYPE.columnName)],
        dateOfCreation = rs.getTimestamp(ReviewColumn.DATE_OF_CREATION.columnName)
            .toLocalDateTime()
    )

    private fun checkIdValidation(id: Int): Boolean = id != ZERO_VALUE
}
