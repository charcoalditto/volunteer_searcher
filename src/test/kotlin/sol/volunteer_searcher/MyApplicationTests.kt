package sol.volunteer_searcher

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import sol.volunteer_searcher.config.IndexProps
import sol.volunteer_searcher.service.SearchService

@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class)
@SpringBootTest("my.search.index-prefix=test1")
class MyApplicationTests {
    @Autowired
    lateinit var searchService: SearchService

    @Test
    fun contextLoads() {
    }

    @Test
    fun createIndexTest() {
        val props = IndexProps(
            indexName = "index-name",
            alias = "alias",
            aliasWritable = true,
            settings = listOf("person-settings.json"),
            mapping = "person-mappings.json",
        )
        Assertions.assertFalse(searchService.existsIndex(props.indexName))
        //인덱스 생성
        Assertions.assertTrue(searchService.createIndex(props) == true)
        Assertions.assertTrue(searchService.existsIndex(props.indexName))
        //인덱스 삭제
        Assertions.assertTrue(searchService.deleteIndex(props.indexName))
        Assertions.assertFalse(searchService.existsIndex(props.indexName))
    }
}
