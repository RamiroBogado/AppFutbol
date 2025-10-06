package com.example.appfutbol.repository

import com.example.appfutbol.configurations.RetrofitClient
import com.example.appfutbol.dtos.GoleadoresDTO

class GoleadoresRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerGoleadores(): GoleadoresDTO {
        return apiService.getGoleadores()
    }
}