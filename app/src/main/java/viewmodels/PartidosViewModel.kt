package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dtos.FullTimeScore
import dtos.Match
import models.Partido
import repositories.PartidosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PartidosViewModel : ViewModel() {
    private val repository = PartidosRepository()
    private val _partidosState = MutableStateFlow<PartidosState>(PartidosState.Loading)
    val partidosState: StateFlow<PartidosState> = _partidosState.asStateFlow()

    fun cargarPartidosRecientes(competition: String = "PL") {
        viewModelScope.launch {
            _partidosState.value = PartidosState.Loading
            try {
                val response = repository.obtenerPartidosRecientes(competition)
                _partidosState.value = PartidosState.Success(response.matches)
            } catch (e: Exception) {
                _partidosState.value = PartidosState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun cargarProximosPartidos(competition: String = "PL") {
        viewModelScope.launch {
            _partidosState.value = PartidosState.Loading
            try {
                val response = repository.obtenerProximosPartidos(competition)
                _partidosState.value = PartidosState.Success(response.matches)
            } catch (e: Exception) {
                _partidosState.value = PartidosState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Función para convertir Match (DTO) a Partido
    fun convertirMatchesAPartidos(matches: List<Match>): MutableList<Partido> {
        return matches.map { match ->
            Partido(
                id = match.id,
                fecha = formatearFecha(match.utcDate),
                hora = formatearHora(match.utcDate),
                equipoLocal = match.homeTeam.name,
                equipoVisitante = match.awayTeam.name,
                resultado = formatearResultado(match.score.fullTime)
            )
        }.toMutableList()
    }

    private fun formatearFecha(utcDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(utcDate)

            date?.let { outputFormat.format(it) } ?: "Fecha no disponible"
        } catch (_: Exception) {
            "Fecha no disponible"
        }
    }

    private fun formatearHora(utcDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = inputFormat.parse(utcDate)

            date?.let { outputFormat.format(it) } ?: "Hora no disponible"
        } catch (_: Exception) {
            "Hora no disponible"
        }
    }

    private fun formatearResultado(fullTime: FullTimeScore?): String {
        return if (fullTime?.home != null && fullTime.away != null) {
            "${fullTime.home} - ${fullTime.away}"
        } else {
            "-"
        }
    }

    sealed class PartidosState {
        object Loading : PartidosState()
        data class Success(val partidos: List<Match>) : PartidosState()
        data class Error(val mensaje: String) : PartidosState()
    }
}