package com.example.appfutbol.repository

import com.example.appfutbol.configurations.RetrofitClient
import com.example.appfutbol.dtos.PartidosDTO

class PartidosRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerPartidos(): PartidosDTO {
        return apiService.getPartidos()
    }
}