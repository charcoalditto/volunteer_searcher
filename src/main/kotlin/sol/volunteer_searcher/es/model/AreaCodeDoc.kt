package sol.volunteer_searcher.es.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class AreaCodeDoc(
    val sidoName: String,
    val sidoCode: Int,
    val sidoChoseong: String,
    val sidoDecomposition: String,

    val gugunName: String,
    val gugunCode: Int,
    val gugunChoseong: String,
    val gugunDecomposition: String,
) {
    @JsonIgnore
    fun getId() = "${sidoCode}:${gugunCode}"
}
