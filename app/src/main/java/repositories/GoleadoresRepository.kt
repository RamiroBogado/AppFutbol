package repositories

import configurations.RetrofitClient
import dtos.GoleadoresDTO

class GoleadoresRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerGoleadores(competition: String = "PL"): GoleadoresDTO {
        return apiService.getGoleadores(competition)
    }
}