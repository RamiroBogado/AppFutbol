package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentSeason(
    val currentMatchday: Int
)