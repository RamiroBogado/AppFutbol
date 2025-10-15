package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Competition(
    val id: Int,
    val name: String,
    val code: String,
    val type: String,
    val emblem: String?
)
