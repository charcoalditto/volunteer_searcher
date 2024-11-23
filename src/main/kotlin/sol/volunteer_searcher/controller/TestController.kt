package sol.volunteer_searcher.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping
    fun testResult(): Map<String, Any> {
        return mapOf("result" to true)
    }
}

