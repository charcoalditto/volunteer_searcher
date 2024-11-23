package sol.volunteer_searcher

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<VolunteerSearcherApplication>().with(TestcontainersConfiguration::class).run(*args)
}
