package sol.volunteer_searcher.service

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import sol.volunteer_searcher.config.IndexProps


@Service
class AreaCodeService(
    private val areaCodeProps: IndexProps,
    private val searchService: SearchService
) {


    @PostConstruct
    fun init() {
        // 프로젝트 인덱스 자동 생성
        searchService.prepareIndex(areaCodeProps)
    }
}
