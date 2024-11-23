package sol.volunteer_searcher.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig {
    @Bean
    fun springOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("volunteer_searcher")
                    .description("")
                    .version("")
            )
    }
}
