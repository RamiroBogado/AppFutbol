package com.example.appfutbol.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfutbol.dtos.Match
import com.example.appfutbol.repository.PartidosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    sealed class PartidosState {
        object Loading : PartidosState()
        data class Success(val partidos: List<Match>) : PartidosState()
        data class Error(val mensaje: String) : PartidosState()
    }
}