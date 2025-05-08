package pet.articles.config

import org.springframework.beans.factory.annotation.Value

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class FlywayConfig (
    @Value("\${flyway.locations}") private val flywayLocations: String,
    private val dataSource: DataSource
) {

    @Bean
    fun flyway(): Flyway = Flyway.configure()
        .locations(flywayLocations)
		.dataSource(dataSource)
        .load()

    @Bean
    fun flywayMigrate(flyway: Flyway): MigrateResult = flyway.migrate()
}
