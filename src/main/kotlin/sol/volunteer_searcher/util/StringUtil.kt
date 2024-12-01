package sol.volunteer_searcher.util

import java.text.Normalizer
import org.apache.commons.lang3.StringUtils
import org.springframework.core.io.ClassPathResource

fun readResource(path: String): String {
    return ClassPathResource(path).inputStream.use { input ->
        input.bufferedReader().use { it.readText() }
    }
}

val initialConsonants = listOf(
    'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
    'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
)

/**
 * 초성
 */
fun String.extractInitialConsonants(): String? {
    return this
        .map { char ->
            if (StringUtils.isAlpha(char.toString())) {
                // 한글이 아닌 경우 (예: 알파벳 등) 필터링 처리
                null
            } else if (char in '가'..'힣') {
                val unicodeOffset = char - '가'
                val initialIndex = unicodeOffset / (21 * 28)
                initialConsonants[initialIndex]
            } else {
                null // 한글이 아닌 기타 문자 처리
            }
        }
        .filterNotNull()
        .joinToString("")
        .takeIf { it.isNotBlank() }
}

/**
 * 초성 중성 종성
 */
fun String.decomposeHangul(): String? {
    return this
        .map { char ->
            if (char in '가'..'힣') {
                // Unicode Normalization을 통해 자모로 분리
                val decomposed: String = Normalizer.normalize(char.toString(), Normalizer.Form.NFD)
                decomposed.filter { it.isLetter() } // 분해된 자모만 필터링
            } else {
                emptyList<String>()// 한글이 아닌 기타 문자 처리
            }
        }
        .joinToString("")
        .takeIf { it.isNotBlank() }
}

