package pet.articles.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataSourceConfig(
        @Value("\${datasource.url}") private val dataSourceUrl: String,
        @Value("\${datasource.username}") private val dataSourceUsername: String,
        @Value("\${datasource.password}") private val dataSourcePassword: String
) {

    @Bean
    fun dataSource(): HikariDataSource =
        HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = dataSourceUrl
                username = dataSourceUsername
                password = dataSourcePassword
            }
        )
}
