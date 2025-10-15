package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coach(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val name: String,
    val dateOfBirth: String,
    val nationality: String,
)
