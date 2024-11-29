package sol.volunteer_searcher.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class IndexConfig {

    @Bean
    @ConfigurationProperties(prefix = "my.search.index.area-code")
    fun areaCodeProps() = IndexProps()
}
