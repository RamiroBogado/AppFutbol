package com.example.appfutbol.repository

import com.example.appfutbol.configurations.RetrofitClient
import com.example.appfutbol.dtos.PartidosDTO

class PartidosRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerJornadaActual(): Int {
        val response = apiService.getCompetencia()
        return response.currentSeason.currentMatchday
    }

    suspend fun obtenerPartidosRecientes(): PartidosDTO {
        val jornadaActual = obtenerJornadaActual()
        return apiService.getPartidos(jornadaActual) // Jornada actual
    }

    suspend fun obtenerProximosPartidos(): PartidosDTO {
        val jornadaActual = obtenerJornadaActual()
        return apiService.getPartidos(jornadaActual + 1) // Jornada proxima
    }
}