package com.efisense.chapurdemo.domain.usecase

import com.efisense.chapurdemo.domain.model.Country
import com.efisense.chapurdemo.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener todos los países.
 * Encapsula la lógica de negocio relacionada con la obtención del listado completo de países.
 * Sigue el principio de responsabilidad única (SRP).
 *
 * @param repository El repositorio de países para acceder a los datos
 */
class GetAllCountriesUseCase(
    private val repository: CountryRepository
) {
    /**
     * Ejecuta el caso de uso para obtener todos los países.
     *
     * @return Flow que emite un Result con la lista de países o un error
     */
    operator fun invoke(): Flow<Result<List<Country>>> {
        return repository.getAllCountries()
    }
}
