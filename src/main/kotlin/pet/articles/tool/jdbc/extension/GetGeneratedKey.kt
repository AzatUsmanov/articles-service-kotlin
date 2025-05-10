package pet.articles.tool.jdbc.extension

import java.sql.PreparedStatement
import java.sql.SQLException

fun PreparedStatement.getGeneratedKey(): Int =
    generatedKeys.use { keys ->
        if (!keys.next()) {
            throw SQLException("Failed to get generated ID")
        }
        keys.getInt(1)
    }