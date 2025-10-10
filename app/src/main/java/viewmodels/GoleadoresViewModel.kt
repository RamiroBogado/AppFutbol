package com.example.appfutbol.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfutbol.models.Goleador
import com.example.appfutbol.repositories.GoleadoresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoleadoresViewModel : ViewModel() {
    private val repository = GoleadoresRepository()
    private val _goleadoresState = MutableStateFlow<GoleadoresState>(GoleadoresState.Loading)
    val goleadoresState: StateFlow<GoleadoresState> = _goleadoresState.asStateFlow()

    fun cargarGoleadores(competition: String = "PL") {
        viewModelScope.launch {
            _goleadoresState.value = GoleadoresState.Loading
            try {
                val response = repository.obtenerGoleadores(competition)
                val goleadoresConvertidos = convertirScorersAGoleadores(response.scorers)
                _goleadoresState.value = GoleadoresState.Success(goleadoresConvertidos)
            } catch (e: Exception) {
                _goleadoresState.value = GoleadoresState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private fun convertirScorersAGoleadores(scorers: List<com.example.appfutbol.dtos.Scorer>): List<Goleador> {
        return scorers.map { scorer ->
            Goleador(
                nombre = scorer.player.name,
                equipo = scorer.team.name,
                goles = scorer.goals,
                asistencias = scorer.assists,
                partidosJugados = scorer.playedMatches
            )
        }
    }

    sealed class GoleadoresState {
        object Loading : GoleadoresState()
        data class Success(val goleadores: List<Goleador>) : GoleadoresState()
        data class Error(val mensaje: String) : GoleadoresState()
    }
}