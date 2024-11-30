package sol.volunteer_searcher.util

import org.springframework.core.io.ClassPathResource

fun readResource(path: String): String {
    return ClassPathResource(path).inputStream.use { input ->
        input.bufferedReader().use { it.readText() }
    }
}

