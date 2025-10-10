package com.example.appfutbol.dtos

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class Scorer(
    val player: Player,
    val team: Team,
    val playedMatches: Int,
    val goals: Int,
    val assists: Int?,
)