package com.efisense.chapurdemo.domain.usecase

import com.efisense.chapurdemo.domain.model.Country
import com.efisense.chapurdemo.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para buscar países por nombre.
 * Encapsula la lógica de negocio relacionada con la búsqueda de países.
 * Sigue el principio de responsabilidad única (SRP).
 *
 * @param repository El repositorio de países para acceder a los datos
 */
class SearchCountriesUseCase(
    private val repository: CountryRepository
) {
    /**
     * Ejecuta el caso de uso para buscar países por nombre.
     *
     * @param query El texto de búsqueda (nombre parcial o completo del país)
     * @return Flow que emite un Result con la lista de países encontrados o un error
     */
    operator fun invoke(query: String): Flow<Result<List<Country>>> {
        return repository.searchCountriesByName(query)
    }
}

