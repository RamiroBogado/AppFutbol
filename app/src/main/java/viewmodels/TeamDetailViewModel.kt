package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dtos.TeamDetailDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import repositories.TeamDetailRepository

class TeamDetailViewModel : ViewModel() {

    private val repository = TeamDetailRepository()
    private val _teamDetailState = MutableStateFlow<TeamDetailState>(TeamDetailState.Loading)
    val teamDetailState: StateFlow<TeamDetailState> = _teamDetailState.asStateFlow()

    fun cargarDetalleEquipo(teamId: Int) {
        viewModelScope.launch {
            _teamDetailState.value = TeamDetailState.Loading
            try {
                val teamDetail = repository.obtenerDetalleEquipo(teamId)
                _teamDetailState.value = TeamDetailState.Success(teamDetail)
            } catch (e: Exception) {
                _teamDetailState.value = TeamDetailState.Error("Error al cargar detalles: ${e.message}")
            }
        }
    }

    sealed class TeamDetailState {
        object Loading : TeamDetailState()
        data class Success(val teamDetail: TeamDetailDTO) : TeamDetailState()
        data class Error(val mensaje: String) : TeamDetailState()
    }
}