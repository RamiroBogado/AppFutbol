package dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamDetailDTO(
    val id: Int,
    val name: String,
    val shortName: String,
    val crest: String,
    val address: String,
    val website: String,
    val founded: Int,
    val clubColors: String,
    val venue: String,
    val runningCompetitions: List<Competition>,
    val coach: Coach?,
    val squad: List<PlayerSquad>
)
