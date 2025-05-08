package pet.articles.tool.db

import org.springframework.stereotype.Component

import pet.articles.model.dto.User
import pet.articles.model.enums.UserColumn
import pet.articles.model.enums.UserRole

import java.sql.ResultSet

@Component
class UserMapper : RowMapper<User> {

    override fun mapRow(rs: ResultSet): User = User(
        id = rs.getInt(UserColumn.ID.columnName),
        username = rs.getString(UserColumn.USERNAME.columnName),
        email = rs.getString(UserColumn.EMAIL.columnName),
        password = rs.getString(UserColumn.PASSWORD.columnName),
        role = UserRole.entries[rs.getInt(UserColumn.ROLE.columnName)]
    )
}