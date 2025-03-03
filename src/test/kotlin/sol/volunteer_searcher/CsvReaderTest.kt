package sol.volunteer_searcher

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import sol.volunteer_searcher.util.CsvMapper

class CsvReaderTest {
    @Test
    fun csvTest() {
        val result = CsvMapper.readCsvFile("test.csv")
        Assertions.assertEquals(setOf("name1", "name2"), result.records.first().keys)
        Assertions.assertEquals("val1", result.records[0]["name1"])

        Assertions.assertEquals("한국어1", result.records[1]["name1"])
        Assertions.assertEquals("ㅎㄱㅇ2", result.records[2]["name2"])
        Assertions.assertEquals(3, result.records.count())
    }
}
