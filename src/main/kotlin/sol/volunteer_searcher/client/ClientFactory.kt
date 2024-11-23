package sol.volunteer_searcher.client

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.WebClient

object ClientFactory {
    fun createClient(
        baseUrl: String,
        mapper: ObjectMapper,
    ): WebClient {
        val webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .codecs { configurer ->
                val codecs = configurer.defaultCodecs()
                codecs.maxInMemorySize(-1)
                codecs.jackson2JsonEncoder(Jackson2JsonEncoder(mapper))
                codecs.jackson2JsonDecoder(Jackson2JsonDecoder(mapper))
            }
            .build()
        return webClient
    }
}

val mapper: ObjectMapper = jacksonObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .registerModule(JavaTimeModule())
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)

val includeNullMapper: ObjectMapper = jacksonObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .registerModule(JavaTimeModule())
