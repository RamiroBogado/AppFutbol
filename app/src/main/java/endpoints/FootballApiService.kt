package endpoints

import dtos.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FootballApiService {

    @GET("competitions/{competition}/matches")
    suspend fun getPartidos(
        @Path("competition") competition: String,
        @Query("matchday") matchday: Int
    ): PartidosDTO

    @GET("competitions/{competition}/scorers")
    suspend fun getGoleadores(@Path("competition") competition: String): GoleadoresDTO

    @GET("competitions/{competition}/standings")
    suspend fun getTablaPosiciones(@Path("competition") competition: String): TablaPosicionesDTO

    @GET("competitions/{competition}")
    suspend fun getCompetencia(@Path("competition") competition: String): CompetenciaDTO

    @GET("persons/{id}")
    suspend fun getDetalleJugador(@Path("id") playerId: Int): PlayerDetailDTO

    @GET("teams/{id}")
    suspend fun getDetalleEquipo(@Path("id") teamId: Int): TeamDetailDTO
}