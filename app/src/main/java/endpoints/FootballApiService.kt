package com.example.appfutbol.endpoints

import com.example.appfutbol.dtos.CompetenciaDTO
import com.example.appfutbol.dtos.GoleadoresDTO
import com.example.appfutbol.dtos.PartidosDTO
import com.example.appfutbol.dtos.TablaPosicionesDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballApiService {

    @GET("PL")
    suspend fun getCompetencia(): CompetenciaDTO

    @GET("PL/matches")
    suspend fun getPartidos(@Query("matchday") matchday: Int): PartidosDTO

    @GET("PL/scorers")
    suspend fun getGoleadores(): GoleadoresDTO

    @GET("PL/standings")
    suspend fun getTablaPosiciones(): TablaPosicionesDTO
}