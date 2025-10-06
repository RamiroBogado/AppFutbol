package com.example.appfutbol.repository

import com.example.appfutbol.configurations.RetrofitClient
import com.example.appfutbol.dtos.GoleadoresResponse

class GoleadoresRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerGoleadores(): GoleadoresResponse {
        return apiService.getGoleadores()
    }
}