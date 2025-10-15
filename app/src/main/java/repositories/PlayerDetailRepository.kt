package repositories

import configurations.RetrofitClient
import dtos.PlayerDetailDTO

class PlayerDetailRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerDetalleJugador(playerId: Int): PlayerDetailDTO {
        val url = "https://api.football-data.org/v4/persons/$playerId"
        return apiService.getDetalleJugador(url)
    }
}