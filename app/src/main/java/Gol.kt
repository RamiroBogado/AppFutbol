package com.example.appfutbol

import java.io.Serializable

data class Gol(
    val minuto: Int,
    val equipo: String,
    val jugador: String,
    val numeroGol: Int
) : Serializable