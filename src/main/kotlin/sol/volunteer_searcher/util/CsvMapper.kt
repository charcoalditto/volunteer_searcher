package sol.volunteer_searcher.util

import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader

data class CsvMapper(
    val fileName: String,
    val records: List<Map<String, String>>,
    val header: List<String>,
) {
    companion object {
        /**
         * key = header, value: value
         */
        fun readCsvFile(fileName: String): CsvMapper {
            val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream(fileName)
                ?: throw IllegalArgumentException("File not found: $fileName")

            val reader = CSVReader(InputStreamReader(inputStream))
            val records = mutableListOf<Map<String, String>>()

            val header: Array<String> = reader.readNext() ?: throw IllegalArgumentException("CSV 파일에 헤더가 없습니다.")

            var line: Array<String>?
            while (reader.readNext().also { line = it } != null) {
                val record = header.zip(line!!).toMap()
                records.add(record)
            }
            reader.close()

            return CsvMapper(
                fileName = fileName,
                header = header.toList(),
                records = records,
            )
        }
    }

}
