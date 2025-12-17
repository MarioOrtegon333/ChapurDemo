package com.efisense.chapurdemo.presentation.countrylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efisense.chapurdemo.domain.usecase.GetAllCountriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de listado de países.
 * Sigue el patrón MVVM y gestiona el estado de la UI de forma reactiva usando StateFlow.
 *
 * @param getAllCountriesUseCase Caso de uso para obtener todos los países
 */
class CountryListViewModel(
    private val getAllCountriesUseCase: GetAllCountriesUseCase
) : ViewModel() {

    /**
     * Estado mutable privado de la UI.
     * Solo el ViewModel puede modificar este estado.
     */
    private val _uiState = MutableStateFlow<CountryListUiState>(CountryListUiState.Loading)

    /**
     * Estado público de la UI como StateFlow inmutable.
     * La UI observa este Flow para reaccionar a cambios de estado.
     */
    val uiState: StateFlow<CountryListUiState> = _uiState.asStateFlow()

    init {
        // Cargar países automáticamente al inicializar el ViewModel
        loadCountries()
    }

    /**
     * Carga la lista de países desde la API.
     * Actualiza el estado de la UI según el resultado obtenido.
     */
    fun loadCountries() {
        viewModelScope.launch {
            _uiState.value = CountryListUiState.Loading

            getAllCountriesUseCase().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { countries ->
                        CountryListUiState.Success(countries)
                    },
                    onFailure = { exception ->
                        CountryListUiState.Error(
                            exception.message ?: "Error desconocido al cargar los países"
                        )
                    }
                )
            }
        }
    }

    /**
     * Reintenta cargar la lista de países.
     * Se usa cuando el usuario quiere reintentar después de un error.
     */
    fun retry() {
        loadCountries()
    }
}

