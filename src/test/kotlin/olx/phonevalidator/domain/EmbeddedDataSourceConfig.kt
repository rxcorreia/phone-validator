package olx.phonevalidator.domain

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@TestConfiguration
class EmbeddedDataSourceConfig {

    @Bean
    fun dataSource(): DataSource {
        return embeddedPostgres().postgresDatabase
    }

    @Bean(destroyMethod = "close")
    fun embeddedPostgres(): EmbeddedPostgres {
        return EmbeddedPostgres.start()
    }
}