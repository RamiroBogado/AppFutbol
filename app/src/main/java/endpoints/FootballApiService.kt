package com.example.appfutbol.endpoints

import com.example.appfutbol.dtos.PartidosDTO
import retrofit2.http.GET

interface FootballApiService {

    @GET("PL/matches?matchday=7")
    suspend fun getPartidos(): PartidosDTO

}