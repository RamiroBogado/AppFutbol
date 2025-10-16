package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Standing(
    val table: List<TeamStanding>
)
