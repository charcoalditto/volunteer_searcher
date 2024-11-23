package sol.volunteer_searcher.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono


@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping
    fun testResult(): Mono<Map<String, Any>> {
        return mapOf("result" to true).toMono()
    }
}

