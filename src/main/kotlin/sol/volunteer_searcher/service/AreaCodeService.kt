package sol.volunteer_searcher.service

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import sol.volunteer_searcher.client.datago.AreaCode
import sol.volunteer_searcher.client.datago.AreaCodeRequest
import sol.volunteer_searcher.client.datago.CodeClient
import sol.volunteer_searcher.client.datago.CodeResponse
import sol.volunteer_searcher.config.IndexProps
import sol.volunteer_searcher.es.model.AreaCodeDoc
import sol.volunteer_searcher.util.logger


@Service
class AreaCodeService(
    private val codeClient: CodeClient,
    private val areaCodeProps: IndexProps,
    private val searchService: SearchService
) {
    private val logger = logger()
    private val chunkSize = 100

    @PostConstruct
    fun init() {
        // 프로젝트 인덱스 자동 생성
        searchService.prepareIndex(areaCodeProps)
    }


    suspend fun update() {
        fetchAllPages(1)
            .awaitSingleOrNull()
    }

    fun fetchPage(page: Int): Mono<CodeResponse.Body<AreaCode>> {
        return codeClient.getAreaCodes(AreaCodeRequest(page = page, size = chunkSize))
            .doOnNext {
                logger.debug("request page: $page, size: $chunkSize")
            }
            .map { it.get() }
            .doOnNext { response: CodeResponse.Body<AreaCode> ->
                response.items?.item?.map { AreaCodeDoc.of(it) }
                    ?.let { docs: List<AreaCodeDoc> ->
                        searchService.insert(areaCodeProps.indexName, docs)
                        logger.debug("doc inserted size ${docs.size}")
                    }
            }
    }

    fun fetchAllPages(currentPage: Int): Mono<String> {
        return fetchPage(currentPage)
            .flatMap { response: CodeResponse.Body<AreaCode> ->
                val isLastPage = ((response.items?.item?.size ?: 0) < chunkSize)
                if (!isLastPage) {
                    fetchAllPages(currentPage + 1) // 다음 페이지 호출
                } else {
                    Mono.empty() // 마지막 페이지일 경우 종료
                }
            }
    }
}

