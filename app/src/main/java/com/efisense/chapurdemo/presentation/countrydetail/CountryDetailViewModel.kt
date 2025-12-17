package com.efisense.chapurdemo.presentation.countrydetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efisense.chapurdemo.domain.usecase.GetCountryByCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de detalle de país.
 * Sigue el patrón MVVM y gestiona el estado de la UI de forma reactiva usando StateFlow.
 * Recibe el código del país a través de SavedStateHandle desde Navigation.
 *
 * @param getCountryByCodeUseCase Caso de uso para obtener un país por su código
 * @param savedStateHandle Handle para acceder a los argumentos de navegación
 */
class CountryDetailViewModel(
    private val getCountryByCodeUseCase: GetCountryByCodeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Código del país extraído de los argumentos de navegación.
     */
    private val countryCode: String = savedStateHandle.get<String>("countryCode") ?: ""

    /**
     * Estado mutable privado de la UI.
     * Solo el ViewModel puede modificar este estado.
     */
    private val _uiState = MutableStateFlow<CountryDetailUiState>(CountryDetailUiState.Loading)

    /**
     * Estado público de la UI como StateFlow inmutable.
     * La UI observa este Flow para reaccionar a cambios de estado.
     */
    val uiState: StateFlow<CountryDetailUiState> = _uiState.asStateFlow()

    init {
        // Cargar el país automáticamente al inicializar el ViewModel
        loadCountryDetail()
    }

    /**
     * Carga los detalles del país desde la API.
     * Actualiza el estado de la UI según el resultado obtenido.
     */
    fun loadCountryDetail() {
        if (countryCode.isBlank()) {
            _uiState.value = CountryDetailUiState.Error("Código de país no válido")
            return
        }

        viewModelScope.launch {
            _uiState.value = CountryDetailUiState.Loading

            getCountryByCodeUseCase(countryCode).collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { country ->
                        CountryDetailUiState.Success(country)
                    },
                    onFailure = { exception ->
                        CountryDetailUiState.Error(
                            exception.message ?: "Error desconocido al cargar el país"
                        )
                    }
                )
            }
        }
    }

    /**
     * Reintenta cargar los detalles del país.
     * Se usa cuando el usuario quiere reintentar después de un error.
     */
    fun retry() {
        loadCountryDetail()
    }
}

