package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerSquad(
    val id: Int,
    val name: String,
    val position: String,
    val dateOfBirth: String,
    val nationality: String
)
