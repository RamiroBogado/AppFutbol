package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerDetailDTO(
    val id: Int,
    val name: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val nationality: String,
    val section: String
)
