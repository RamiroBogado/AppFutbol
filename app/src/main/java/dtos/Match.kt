package com.example.appfutbol.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Match(
    val id: Int,
    val utcDate: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score
)
