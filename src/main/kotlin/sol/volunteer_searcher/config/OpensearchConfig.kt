package sol.volunteer_searcher.config

import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.opensearch.client.RestClient
import org.opensearch.client.RestClientBuilder
import org.opensearch.client.RestHighLevelClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.PropertyMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(
    OpensearchConfig.OpensearchClientProperties::class
)
class OpensearchConfig {

    @ConfigurationProperties(prefix = "my.opensearch")
    class OpensearchClientProperties(
        val uris: List<String>,
        val username: String? = null,
        val password: String? = null
    )

    @Bean
    fun restClientBuilderCustomizer(properties: OpensearchClientProperties): RestHighLevelClient {
        val map = PropertyMapper.get()

        val builder: RestClientBuilder = RestClient.builder(*properties.uris.map { HttpHost.create(it) }.toTypedArray())
            .setCompressionEnabled(true)
            .setHttpClientConfigCallback { httpClientBuilder ->
                httpClientBuilder.run {
                    if (!properties.username.isNullOrBlank() && !properties.password.isNullOrBlank()) {
                        setDefaultCredentialsProvider(
                            BasicCredentialsProvider().apply {
                                setCredentials(AuthScope.ANY, UsernamePasswordCredentials(properties.username, properties.password))
                            }
                        )
                    }
                    this
                }
            }

        return RestHighLevelClient(builder)
    }
}

data class IndexProps(
    val indexName: String,
    val alias: String,
    val aliasWritable: Boolean = false,
    val settings: List<String>? = null,
    val mapping: String? = null,
)
