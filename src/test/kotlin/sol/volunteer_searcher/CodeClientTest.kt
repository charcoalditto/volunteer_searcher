package sol.volunteer_searcher


import kotlin.jvm.optionals.getOrNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension
import sol.volunteer_searcher.client.datago.AreaCode
import sol.volunteer_searcher.client.datago.CodeClient
import sol.volunteer_searcher.client.datago.CodeResponse
import sol.volunteer_searcher.client.datago.DatagoConfig
import sol.volunteer_searcher.client.datago.VltrRealmCode


@ActiveProfiles("test")
@SpringBootTest(
    classes = [DatagoConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ExtendWith(SpringExtension::class)
class CodeClientTest {

    @Autowired
    lateinit var datagoProps: DatagoConfig.DatagoProps

    val client: CodeClient by lazy {
        CodeClient(datagoProps)
    }

    @Test
    fun getVltrRealmCodesTest() {
        val response: CodeResponse.Body<VltrRealmCode>? = client.getVltrRealmCodes()
            .block()
            ?.getOrNull()

        Assertions.assertTrue((response?.totalCount ?: 0) > 0)
    }

    @Test
    fun getAreaCodeTest() {
        val response: CodeResponse.Body<AreaCode>? = client.getAreaCodes()
            .block()
            ?.getOrNull()

        Assertions.assertTrue((response?.totalCount ?: 0) > 0)
        Assertions.assertTrue(response?.items?.item?.first()?.gugunNm != null)
    }
}
