package com.example.appfutbol.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetenciaDTO(
    val currentSeason: CurrentSeason
)