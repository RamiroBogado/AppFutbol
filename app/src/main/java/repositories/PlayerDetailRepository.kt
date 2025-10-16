package repositories

import configurations.RetrofitClient
import dtos.PlayerDetailDTO

class PlayerDetailRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerDetalleJugador(playerId: Int): PlayerDetailDTO {
        return apiService.getDetalleJugador(playerId)
    }
}