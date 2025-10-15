package models

data class Goleador(
    val id: Int,
    val nombre: String,
    val equipo: String,
    val goles: Int,
    val asistencias: Int?,
    val partidosJugados: Int
)