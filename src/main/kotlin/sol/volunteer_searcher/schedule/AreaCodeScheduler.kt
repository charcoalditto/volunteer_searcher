package sol.volunteer_searcher.schedule

import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import sol.volunteer_searcher.service.AreaCodeService
import sol.volunteer_searcher.util.logger

@Component
class AreaCodeScheduler(
    private val service: AreaCodeService,
) {
    private val logger = logger()

    @Scheduled(
        fixedRate = 1000 * 60 * 60 * 24, // 하루에 1번
        initialDelay = 0,
    )
    fun updateAreaCode() = runBlocking {
        logger.info("refresh start")
        service.update()
        logger.info("refresh end")
    }
}
