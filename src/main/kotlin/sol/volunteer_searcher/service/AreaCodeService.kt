package sol.volunteer_searcher.service

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
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
        for (page in 1..100) {
            val response: CodeResponse.Body<AreaCode> = codeClient.getAreaCodes(AreaCodeRequest(page = page, size = chunkSize))
                .awaitSingle()
                .get()

            val docs = response.items?.item?.map { AreaCodeDoc.of(it) } ?: break
            searchService.insert(areaCodeProps.indexName, docs)

            logger.debug("doc inserted size ${docs.size}")

            if ((response.items?.item?.size ?: 0) < chunkSize) break
        }
    }

//    fun fetchPage(page: Int): Mono<CodeResponse.Body<AreaCode>> {
//        return codeClient.getAreaCodes(AreaCodeRequest(page = page, size = 1000))
//            .map { it.get() }
//            .subscribeOn(Schedulers.boundedElastic())
//    }
//
//    fun fetchAllPages(currentPage: Int = 0): Mono<String> {
//        return fetchPage(currentPage)
//            .map {
//                it
//            }
//            .flatMap { response: CodeResponse.Body<AreaCode> ->
//                val isLastPage = ((response.numOfRows ?: 0) < chunkSize)
//                if (!isLastPage) {
//                    fetchAllPages(currentPage + 1) // 다음 페이지 호출
//                } else {
//                    Mono.empty() // 마지막 페이지일 경우 종료
//                }
//            }
//    }
}
