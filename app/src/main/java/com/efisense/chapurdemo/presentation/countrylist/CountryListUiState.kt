package com.efisense.chapurdemo.presentation.countrylist

import com.efisense.chapurdemo.domain.model.Country

/**
 * Clase sellada que representa los diferentes estados de la UI para la pantalla de listado de países.
 * Utiliza el patrón de estado sellado para garantizar un manejo exhaustivo de todos los estados posibles.
 */
sealed class CountryListUiState {

    /**
     * Estado inicial o de carga.
     * Se muestra mientras se está obteniendo la información de la API.
     */
    data object Loading : CountryListUiState()

    /**
     * Estado de éxito.
     * Contiene la lista de países obtenida exitosamente con información de paginación.
     *
     * @param countries Lista de países a mostrar en la página actual
     * @param currentPage Página actual (base 1)
     * @param totalPages Total de páginas disponibles
     * @param totalCountries Total de países sin paginar
     */
    data class Success(
        val countries: List<Country>,
        val currentPage: Int = 1,
        val totalPages: Int = 1,
        val totalCountries: Int = 0
    ) : CountryListUiState()

    /**
     * Estado de error.
     * Contiene el mensaje de error para mostrar al usuario.
     *
     * @param message Mensaje descriptivo del error ocurrido
     */
    data class Error(
        val message: String
    ) : CountryListUiState()
}
