package sol.volunteer_searcher.client.datago

import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Validated
@Configuration
@EnableConfigurationProperties(
    DatagoConfig.DatagoProps::class,
)
class DatagoConfig {
    @ConfigurationProperties(prefix = "datago")
    class DatagoProps(
        @NotNull
        val authKey: String,
    )
}
