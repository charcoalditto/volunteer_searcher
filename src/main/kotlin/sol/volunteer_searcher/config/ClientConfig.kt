package sol.volunteer_searcher.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import sol.volunteer_searcher.client.datago.CodeClient
import sol.volunteer_searcher.client.datago.DatagoConfig

@Configuration
class ClientConfig(private val props: DatagoConfig.DatagoProps) {


    @Bean
    fun codeClient(): CodeClient {
        return CodeClient(props)
    }
}
