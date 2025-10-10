package com.example.appfutbol.dtos

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class TeamStanding(
    val position: Int,
    val team: Team,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int
)
