package sol.volunteer_searcher.es.model

import com.fasterxml.jackson.annotation.JsonIgnore
import sol.volunteer_searcher.client.datago.AreaCode
import sol.volunteer_searcher.util.decomposeHangul
import sol.volunteer_searcher.util.extractInitialConsonants

data class AreaCodeDoc(
    val sidoName: String,
    val sidoCode: Int,
    val sidoChoseong: String?,
    val sidoDecomposition: String?,

    val gugunName: String,
    val gugunCode: Int,
    val gugunChoseong: String?,
    val gugunDecomposition: String?,
) : IdGetter {
    companion object {
        fun of(ac: AreaCode): AreaCodeDoc {
            return AreaCodeDoc(
                sidoName = ac.sidoNm,
                sidoCode = ac.sidoCd,
                sidoChoseong = ac.sidoNm.extractInitialConsonants(),
                sidoDecomposition = ac.sidoNm.decomposeHangul(),
                gugunName = ac.gugunNm,
                gugunCode = ac.gugunCd,
                gugunChoseong = ac.gugunNm.extractInitialConsonants(),
                gugunDecomposition = ac.gugunNm.decomposeHangul(),
            )
        }
    }

    @JsonIgnore
    override fun getId() = "${sidoCode}:${gugunCode}"

}
