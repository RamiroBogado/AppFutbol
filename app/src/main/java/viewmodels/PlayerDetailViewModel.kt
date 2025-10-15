package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dtos.PlayerDetailDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import repositories.PlayerDetailRepository

class PlayerDetailViewModel : ViewModel() {

    private val repository = PlayerDetailRepository()
    private val _playerDetailState = MutableStateFlow<PlayerDetailState>(PlayerDetailState.Loading)
    val playerDetailState: StateFlow<PlayerDetailState> = _playerDetailState.asStateFlow()

    fun cargarDetalleJugador(playerId: Int) {
        viewModelScope.launch {
            _playerDetailState.value = PlayerDetailState.Loading
            try {
                val playerDetail = repository.obtenerDetalleJugador(playerId)
                _playerDetailState.value = PlayerDetailState.Success(playerDetail)
            } catch (e: Exception) {
                _playerDetailState.value = PlayerDetailState.Error("Error al cargar detalles: ${e.message}")
            }
        }
    }

    sealed class PlayerDetailState {
        object Loading : PlayerDetailState()
        data class Success(val playerDetail: PlayerDetailDTO) : PlayerDetailState()
        data class Error(val mensaje: String) : PlayerDetailState()
    }
}