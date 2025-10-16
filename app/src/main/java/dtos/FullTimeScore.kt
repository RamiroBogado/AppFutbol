package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FullTimeScore(
    val home: Int?,
    val away: Int?
)
