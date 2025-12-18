package com.efisense.chapurdemo.data.repository

import com.efisense.chapurdemo.data.mapper.CountryMapper
import com.efisense.chapurdemo.data.remote.api.CountryApiService
import com.efisense.chapurdemo.domain.model.Country
import com.efisense.chapurdemo.domain.repository.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

/**
 * Implementación del repositorio de países.
 * Maneja las llamadas a la API y convierte los DTOs en modelos de dominio.
 * Implementa manejo de errores robusto para diferentes tipos de fallos de red.
 *
 * @param apiService El servicio de API de Retrofit para hacer las llamadas HTTP
 */
class CountryRepositoryImpl(
    private val apiService: CountryApiService
) : CountryRepository {

    /**
     * Obtiene todos los países desde la API REST Countries.
     * Utiliza Flow para emitir el resultado de forma reactiva.
     *
     * @return Flow que emite Result.success con la lista de países o Result.failure con el error
     */
    override fun getAllCountries(): Flow<Result<List<Country>>> = flow {
        try {
            val response = apiService.getAllCountries()
            val countries = CountryMapper.fromDtoList(response)
                .sortedBy { it.commonName }
            emit(Result.success(countries))
        } catch (e: IOException) {
            // Error de red (sin conexión, timeout, etc.)
            emit(Result.failure(NetworkException("Error de conexión. Verifica tu conexión a internet.", e)))
        } catch (e: retrofit2.HttpException) {
            // Error HTTP (404, 500, etc.)
            val errorMessage = when (e.code()) {
                404 -> "Recurso no encontrado"
                500 -> "Error del servidor"
                else -> "Error HTTP: ${e.code()}"
            }
            emit(Result.failure(ApiException(errorMessage, e.code(), e)))
        } catch (e: Exception) {
            // Otros errores inesperados
            emit(Result.failure(UnknownException("Error inesperado: ${e.message}", e)))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Obtiene un país específico por su código alfa-3.
     *
     * @param code Código alfa-3 del país
     * @return Flow que emite Result.success con el país o Result.failure con el error
     */
    override fun getCountryByCode(code: String): Flow<Result<Country>> = flow {
        try {
            val response = apiService.getCountryByCode(code)
            val country = CountryMapper.fromDto(response)
            emit(Result.success(country))
        } catch (e: IOException) {
            emit(Result.failure(NetworkException("Error de conexión. Verifica tu conexión a internet.", e)))
        } catch (e: retrofit2.HttpException) {
            val errorMessage = when (e.code()) {
                404 -> "País no encontrado"
                500 -> "Error del servidor"
                else -> "Error HTTP: ${e.code()}"
            }
            emit(Result.failure(ApiException(errorMessage, e.code(), e)))
        } catch (e: Exception) {
            emit(Result.failure(UnknownException("Error inesperado: ${e.message}", e)))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Busca países por nombre usando la API REST Countries.
     *
     * @param query Texto de búsqueda
     * @return Flow que emite Result.success con la lista de países encontrados o Result.failure con el error
     */
    override fun searchCountriesByName(query: String): Flow<Result<List<Country>>> = flow {
        try {
            val response = apiService.searchCountriesByName(query)
            val countries = CountryMapper.fromDtoList(response)
                .sortedBy { it.commonName }
            emit(Result.success(countries))
        } catch (e: IOException) {
            emit(Result.failure(NetworkException("Error de conexión. Verifica tu conexión a internet.", e)))
        } catch (e: retrofit2.HttpException) {
            val errorMessage = when (e.code()) {
                404 -> "No se encontraron países"
                500 -> "Error del servidor"
                else -> "Error HTTP: ${e.code()}"
            }
            emit(Result.failure(ApiException(errorMessage, e.code(), e)))
        } catch (e: Exception) {
            emit(Result.failure(UnknownException("Error inesperado: ${e.message}", e)))
        }
    }.flowOn(Dispatchers.IO)
}

/**
 * Excepción personalizada para errores de red.
 */
class NetworkException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Excepción personalizada para errores de API HTTP.
 */
class ApiException(
    message: String,
    val code: Int,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Excepción personalizada para errores desconocidos.
 */
class UnknownException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

