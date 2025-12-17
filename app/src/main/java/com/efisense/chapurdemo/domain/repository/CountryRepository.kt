package com.efisense.chapurdemo.domain.repository

import com.efisense.chapurdemo.domain.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio de países.
 * Define el contrato para acceder a los datos de países.
 * Esta interfaz pertenece a la capa de dominio y es implementada en la capa de datos.
 */
interface CountryRepository {

    /**
     * Obtiene todos los países disponibles.
     *
     * @return Un [Flow] que emite un [Result] con la lista de países o un error
     */
    fun getAllCountries(): Flow<Result<List<Country>>>

    /**
     * Obtiene un país específico por su código.
     *
     * @param code Código alfa-3 del país (ej: "USA", "ESP", "MEX")
     * @return Un [Flow] que emite un [Result] con el país o un error
     */
    fun getCountryByCode(code: String): Flow<Result<Country>>
}
