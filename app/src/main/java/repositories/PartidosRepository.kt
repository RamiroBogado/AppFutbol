package repositories

import configurations.RetrofitClient
import dtos.PartidosDTO

class PartidosRepository {
    private val apiService = RetrofitClient.getFootballApiService()

    suspend fun obtenerJornadaActual(competition: String = "PL"): Int {
        val response = apiService.getCompetencia(competition)
        return response.currentSeason.currentMatchday

    }

    suspend fun obtenerPartidosRecientes(competition: String = "PL"): PartidosDTO {
        val jornadaActual = obtenerJornadaActual(competition)
        return apiService.getPartidos(competition, jornadaActual-1)
    }

    suspend fun obtenerProximosPartidos(competition: String = "PL"): PartidosDTO {
        val jornadaActual = obtenerJornadaActual(competition)
        return apiService.getPartidos(competition, jornadaActual)
    }


}