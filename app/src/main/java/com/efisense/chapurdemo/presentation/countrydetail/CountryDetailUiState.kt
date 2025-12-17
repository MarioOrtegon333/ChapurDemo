package com.efisense.chapurdemo.presentation.countrydetail

import com.efisense.chapurdemo.domain.model.Country

/**
 * Clase sellada que representa los diferentes estados de la UI para la pantalla de detalle de país.
 * Utiliza el patrón de estado sellado para garantizar un manejo exhaustivo de todos los estados posibles.
 */
sealed class CountryDetailUiState {

    /**
     * Estado inicial o de carga.
     * Se muestra mientras se está obteniendo la información del país.
     */
    data object Loading : CountryDetailUiState()

    /**
     * Estado de éxito.
     * Contiene la información completa del país.
     *
     * @param country El país con todos sus detalles
     */
    data class Success(
        val country: Country
    ) : CountryDetailUiState()

    /**
     * Estado de error.
     * Contiene el mensaje de error para mostrar al usuario.
     *
     * @param message Mensaje descriptivo del error ocurrido
     */
    data class Error(
        val message: String
    ) : CountryDetailUiState()
}

