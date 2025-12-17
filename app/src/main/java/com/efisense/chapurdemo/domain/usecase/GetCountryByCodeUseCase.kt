package com.efisense.chapurdemo.domain.usecase

import com.efisense.chapurdemo.domain.model.Country
import com.efisense.chapurdemo.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener un país específico por su código.
 * Encapsula la lógica de negocio relacionada con la obtención de detalles de un país.
 * Sigue el principio de responsabilidad única (SRP).
 *
 * @param repository El repositorio de países para acceder a los datos
 */
class GetCountryByCodeUseCase(
    private val repository: CountryRepository
) {
    /**
     * Ejecuta el caso de uso para obtener un país por su código.
     *
     * @param code El código alfa-3 del país (ej: "USA", "ESP", "MEX")
     * @return Flow que emite un Result con el país o un error
     */
    operator fun invoke(code: String): Flow<Result<Country>> {
        return repository.getCountryByCode(code)
    }
}

