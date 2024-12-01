package sol.volunteer_searcher

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import sol.volunteer_searcher.config.IndexProps
import sol.volunteer_searcher.service.SearchService

@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest("my.search.index-prefix=test1")
class MyApplicationTests {
    @Autowired
    lateinit var searchService: SearchService

    @Test
    fun contextLoads() {
    }


    val props = IndexProps(
        indexName = "index-name",
        alias = "alias",
        aliasWritable = true,
        settings = listOf("person-settings.json"),
        mapping = "person-mappings.json",
    )


    @BeforeAll
    fun before() {
        //인덱스 생성

        Assertions.assertTrue(searchService.createIndex(props) == true)
    }

    @AfterAll
    fun after() {
        //인덱스 삭제
        Assertions.assertTrue(searchService.deleteIndex(props.indexName))
    }

    @Test
    fun existsIndexTest() {
        Assertions.assertTrue(searchService.existsIndex(props.indexName))
    }

    @Test
    fun insertTest() {
        val response = searchService.insert(
            props.indexName,
            listOf(
                Person("Apple", 42),
                Person("Banana", 17),
                Person("Cherry", 8),
                Person("Date", 25),
                Person("Elderberry", 63),
                Person("Fig", 39),
                Person("Grape", 54),
                Person("Honeydew", 7),
                Person("Kiwi", 91),
                Person("Lemon", 33),
            )
        )
        
        Assertions.assertEquals(10, response.items.count { !it.isFailed })
    }
}
