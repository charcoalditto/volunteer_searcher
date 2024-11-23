package sol.volunteer_searcher.util

import java.util.Optional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

// inline + reified 타입 파라미터를 런타임에 실체화
inline fun <reified T> T?.optionalOrEmpty(): Optional<T & Any> {
    return if (this == null) Optional.empty<T>()
    else Optional.of(this)
}

inline fun <reified T : Any> WebClient.RequestHeadersSpec<*>.request(): Mono<T> {
    return this.retrieve().bodyToMono<T>()
}

inline fun <reified T : Any> WebClient.RequestHeadersSpec<*>.requestAndUnwrap(): Mono<T> {
    return this.retrieve().bodyToMono<T>()
}
