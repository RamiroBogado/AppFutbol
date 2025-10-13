package models

data class Goleador(
    val nombre: String,
    val equipo: String,
    val goles: Int,
    val asistencias: Int?,
    val partidosJugados: Int
)