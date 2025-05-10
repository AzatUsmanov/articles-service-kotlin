package pet.articles.tool.jdbc.mapper

import java.sql.ResultSet

interface RowMapper<T> {

    fun mapRow(rs: ResultSet): T

    fun singleOrNull(rs: ResultSet): T? =
        if (rs.next()) mapRow(rs) else null

    fun list(rs: ResultSet): List<T> =
        generateSequence { if (rs.next()) mapRow(rs) else null }.toList()
}
