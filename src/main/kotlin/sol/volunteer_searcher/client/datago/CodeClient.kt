package sol.volunteer_searcher.client.datago

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import jakarta.validation.constraints.Min
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
        private const val areaCodePath = "/getAreaCodeInquiryList"
    }

    private val client = ClientFactory.createClient(
        baseUrl = baseUrl,
        mapper = mapper,
    )

    /**
     * 자원봉사분야코드조회
     * https://www.data.go.kr/data/15000090/openapi.do#tab_layer_detail_function
     */
    fun getVltrRealmCodes(request: VltrRealmCodeRequest? = null): Mono<Optional<CodeResponse.Body<VltrRealmCode>>> {
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
            .request<CodeResponse>()
            .map {
                mapper.convertValue<CodeResponse.Body<VltrRealmCode>>(it.response.body)
                    .optionalOrEmpty()
            }
    }

    /**
     * 지역코드조회
     * https://www.data.go.kr/data/15000090/openapi.do#tab_layer_prcuse_exam
     */
    fun getAreaCodes(request: AreaCodeRequest? = null): Mono<Optional<CodeResponse.Body<AreaCode>>> {
        return client.get()
            .uri { builder ->
                builder.path(areaCodePath)
                    .queryParam("serviceKey", props.authKey)
                    .queryParamIfPresent("schSido", request?.schSido.optionalOrEmpty())
                    .queryParamIfPresent("schGugun", request?.schGugun.optionalOrEmpty())
                    .queryParamIfPresent("pageNo", request?.page.optionalOrEmpty())
                    .queryParamIfPresent("numOfRows", request?.size.optionalOrEmpty())
                    .build()
            }
            .header("accept", "application/json;charset=UTF-8")
            .request<CodeResponse>()
            .map {
                mapper.convertValue<CodeResponse.Body<AreaCode>>(it.response.body)
                    .optionalOrEmpty()
                // 주의 : 테이터가 없을때 없을때 {"items":""} ..ㅋㅋㅋㅋ
            }
    }
}

inline fun <reified T> ObjectMapper.convertAny(value: Any): T {
    return this.convertValue(value, T::class.java)
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

data class CodeResponse(
    val response: Response,
) {
    data class Response(
        val header: Header,
        val body: BodyAny,
    )

    data class Header(
        val resultCode: String, // 4 필 0000 결과코드
        val resultMsg: String, // 50 필 OK 결과메시지
    )

    data class BodyAny(
        override val items: Items<Any>?,
        override val numOfRows: Int?,
        override val pageNo: Int?,
        override val totalCount: Int?,
    ) : Body<Any>(items, numOfRows, pageNo, totalCount)

    open class Body<T>(
        open val items: Items<T>?,
        open val numOfRows: Int?, // 내가 요청한 한 페이지 사이즈. 문서상 페이지 결과 수
        open val pageNo: Int?, // 5 옵 1 페이지 번호
        open val totalCount: Int?, // 7 옵 3 전체 결과 수
    ) {
        data class Items<T>(
            val item: List<T>?,
        )
    }
}

data class VltrRealmCode(
    val highClsCd: String, // 10 필 1200 상위분류코드
    val highClsNm: String?, // 10 옵 재해재난 상위분류코드명
    val lowClsCd: String?, // 10 옵 1202 하위분류코드
    val lowClsNm: String?, // 20 옵 긴급구조 하위분류코드명
)

data class AreaCode(
    val gugunCd: Int, //구군코드 7자
    val gugunNm: String, //구군명
    val sidoCd: Int, //시도코드 7자
    val sidoNm: String, //시도명
)

data class AreaCodeRequest(
    val schSido: String? = null,// 시도코드 ex) %ec%84%9c%ec%9a%b8(서울)
    val schGugun: String? = null,// 구군명 ex) %ec%a2%85%eb%a1%9c(종로)
    @Min(1)
    val page: Int? = null,
    val size: Int? = null,
)
