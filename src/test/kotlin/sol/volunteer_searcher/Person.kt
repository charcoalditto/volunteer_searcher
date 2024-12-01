package sol.volunteer_searcher

import com.fasterxml.jackson.annotation.JsonIgnore
import sol.volunteer_searcher.es.model.IdGetter

data class Person(
    val name: String,
    val age: Int,
) : IdGetter {
    @JsonIgnore
    override fun getId(): String = name
}

