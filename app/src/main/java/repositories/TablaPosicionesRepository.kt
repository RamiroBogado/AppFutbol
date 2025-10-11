package repositories

import configurations.RetrofitClient
import dtos.TablaPosicionesDTO

class TablaPosicionesRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerTablaPosiciones(competition: String = "PL"): TablaPosicionesDTO {
        return apiService.getTablaPosiciones(competition)
    }
}