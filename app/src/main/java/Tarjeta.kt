package com.example.appfutbol

import java.io.Serializable

data class Tarjeta(
    val minuto: Int,
    val equipo: String,
    val jugador: String,
    val color: String
) : Serializable