package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coach(
    val id: Int,
    val name: String,
)
