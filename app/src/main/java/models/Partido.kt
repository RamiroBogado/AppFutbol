package com.example.appfutbol.models
data class Partido(
    val id: Int,
    val fecha: String,
    val hora: String,
    val equipoLocal: String,
    val equipoVisitante: String,
    val resultado: String
)