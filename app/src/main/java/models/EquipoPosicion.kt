package models

data class EquipoPosicion(
    val id: Int,
    val posicion: Int,
    val nombreEquipo: String,
    val partidosJugados: Int,
    val ganados: Int,
    val empatados: Int,
    val perdidos: Int,
    val puntos: Int
)