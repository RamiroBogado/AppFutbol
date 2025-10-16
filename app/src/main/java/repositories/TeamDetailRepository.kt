package repositories

import configurations.RetrofitClient
import dtos.TeamDetailDTO

class TeamDetailRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerDetalleEquipo(teamId: Int): TeamDetailDTO {
        return apiService.getDetalleEquipo(teamId)
    }
}