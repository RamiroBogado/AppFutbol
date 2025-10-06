package com.example.appfutbol.models

data class EquipoPosicion(
    val posicion: Int,
    val nombreEquipo: String,
    val partidosJugados: Int,
    val ganados: Int,
    val empatados: Int,
    val perdidos: Int,
    val puntos: Int
)