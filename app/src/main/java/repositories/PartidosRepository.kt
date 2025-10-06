package com.example.appfutbol.repository

import com.example.appfutbol.configurations.RetrofitClient
import com.example.appfutbol.dtos.PartidosDTO

class PartidosRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerJornadaActual(competition: String = "PL"): Int {
        val response = apiService.getCompetencia(competition)
        return response.currentSeason.currentMatchday
    }

    suspend fun obtenerPartidosRecientes(competition: String = "PL"): PartidosDTO {
        val jornadaActual = obtenerJornadaActual(competition)
        return apiService.getPartidos(competition, jornadaActual)
    }

    suspend fun obtenerProximosPartidos(competition: String = "PL"): PartidosDTO {
        val jornadaActual = obtenerJornadaActual(competition)
        return apiService.getPartidos(competition, jornadaActual+1)
    }


}