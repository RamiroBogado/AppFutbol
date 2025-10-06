package com.example.appfutbol.repository

import com.example.appfutbol.configurations.RetrofitClient
import com.example.appfutbol.dtos.TablaPosicionesDTO

class TablaPosicionesRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerTablaPosiciones(competition: String = "PL"): TablaPosicionesDTO {
        return apiService.getTablaPosiciones(competition)
    }
}