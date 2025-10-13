package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PartidosDTO(
    val matches: List<Match>
)
