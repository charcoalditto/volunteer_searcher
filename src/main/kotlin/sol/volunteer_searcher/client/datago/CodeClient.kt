package sol.volunteer_searcher.client.datago

import java.util.Optional
import reactor.core.publisher.Mono
import sol.volunteer_searcher.client.ClientFactory
import sol.volunteer_searcher.client.mapper
import sol.volunteer_searcher.util.optionalOrEmpty
import sol.volunteer_searcher.util.request

/**
 * 지역 코드조회
 */
class CodeClient(private val props: DatagoConfig.DatagoProps) {
    companion object {
        private const val baseUrl = "http://openapi.1365.go.kr/openapi/service/rest/CodeInquiryService"
        private const val vltrRealmCodePath = "/getVltrRealmCodeList"
    }

    private val client = ClientFactory.createClient(
        baseUrl = baseUrl,
        mapper = mapper,
    )

    /**
     * 자원봉사분야코드조회
     * https://www.data.go.kr/data/15000090/openapi.do#tab_layer_detail_function
     */
    fun getVltrRealmCodes(request: VltrRealmCodeRequest? = null): Mono<Optional<VltrRealmCodeResponse.Body>> {
        return client.get()
            .uri { builder ->
                builder.path(vltrRealmCodePath)
                    .queryParam("serviceKey", props.authKey)
                    .queryParamIfPresent("clsType", request?.clsType.optionalOrEmpty())
                    .queryParamIfPresent("hignClsNm", request?.hignClsNm.optionalOrEmpty())
                    .queryParamIfPresent("lowClsNm", request?.lowClsNm.optionalOrEmpty())
                    .build()
            }
            .header("accept", "application/json;charset=UTF-8")
            .request<VltrRealmCodeResponse>()
            .map { it.response.body.optionalOrEmpty() }
    }
}

data class VltrRealmCodeRequest(
    /** 상위/하위 구분 */
    val clsType: ClsType?,
    /** 상위분류코드명 */
    val hignClsNm: String?,
    /** 하위분류코드명 */
    val lowClsNm: String?,
) {
    enum class ClsType {
        A, // 상위 코드
        B, // 하위 코드
    }
}

data class VltrRealmCodeResponse(
    val response: Response,
) {
    data class Response(
        val header: Header,
        val body: Body?,
    )

    data class Header(
        val resultCode: String, // 4 필 0000 결과코드
        val resultMsg: String, // 50 필 OK 결과메시지
    )

    data class Body(
        val items: Items?,
        val numOfRows: Int?, // 2 옵 10 한 페이지 결과 수
        val pageNo: Int?, // 5 옵 1 페이지 번호
        val totalCount: Int?, // 7 옵 3 전체 결과 수
    ) {
        data class Items(
            val item: List<VltrRealmCode>?,
        )
    }
}

data class VltrRealmCode(
    val highClsCd: String, // 10 필 1200 상위분류코드
    val highClsNm: String?, // 10 옵 재해재난 상위분류코드명
    val lowClsCd: String?, // 10 옵 1202 하위분류코드
    val lowClsNm: String?, // 20 옵 긴급구조 하위분류코드명
)


