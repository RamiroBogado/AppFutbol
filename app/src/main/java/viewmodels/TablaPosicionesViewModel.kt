package com.example.appfutbol.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfutbol.models.EquipoPosicion
import com.example.appfutbol.repositories.TablaPosicionesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TablaPosicionesViewModel : ViewModel() {
    private val repository = TablaPosicionesRepository()

    private val _tablaPosicionesState = MutableStateFlow<TablaPosicionesState>(TablaPosicionesState.Loading)
    val tablaPosicionesState: StateFlow<TablaPosicionesState> = _tablaPosicionesState.asStateFlow()

    fun cargarTablaPosiciones(competition: String = "PL") {
        viewModelScope.launch {
            _tablaPosicionesState.value = TablaPosicionesState.Loading
            try {
                val response = repository.obtenerTablaPosiciones(competition)
                val tablaConvertida = convertirStandingsAEquipos(response.standings)
                _tablaPosicionesState.value = TablaPosicionesState.Success(tablaConvertida)
            } catch (e: Exception) {
                _tablaPosicionesState.value = TablaPosicionesState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private fun convertirStandingsAEquipos(standings: List<com.example.appfutbol.dtos.Standing>): List<EquipoPosicion> {
        return standings.firstOrNull()?.table?.map { teamStanding ->
            EquipoPosicion(
                posicion = teamStanding.position,
                nombreEquipo = teamStanding.team.name,
                partidosJugados = teamStanding.playedGames,
                ganados = teamStanding.won,
                empatados = teamStanding.draw,
                perdidos = teamStanding.lost,
                puntos = teamStanding.points
            )
        } ?: emptyList()
    }

    sealed class TablaPosicionesState {
        object Loading : TablaPosicionesState()
        data class Success(val equipos: List<EquipoPosicion>) : TablaPosicionesState()
        data class Error(val mensaje: String) : TablaPosicionesState()
    }
}