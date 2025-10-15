package endpoints

import dtos.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface FootballApiService {

    @GET("{competition}/matches")
    suspend fun getPartidos(
        @Path("competition") competition: String,
        @Query("matchday") matchday: Int
    ): PartidosDTO

    @GET("{competition}/scorers")
    suspend fun getGoleadores(@Path("competition") competition: String): GoleadoresDTO

    @GET("{competition}/standings")
    suspend fun getTablaPosiciones(@Path("competition") competition: String): TablaPosicionesDTO

    @GET("{competition}")
    suspend fun getCompetencia(@Path("competition") competition: String): CompetenciaDTO

    @GET
    suspend fun getDetalleJugador(@Url url: String): PlayerDetailDTO
}