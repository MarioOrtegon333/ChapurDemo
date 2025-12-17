package com.efisense.chapurdemo.data.remote.api

import com.efisense.chapurdemo.data.remote.dto.CountryDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz de Retrofit para definir los endpoints de la API REST Countries.
 * Define las operaciones HTTP disponibles para consultar información de países.
 */
interface CountryApiService {

    companion object {
        /**
         * Campos requeridos de la API para el listado de países.
         */
        const val LIST_FIELDS = "name,flags,cca3"

        /**
         * Campos requeridos de la API para el detalle de un país.
         */
        const val DETAIL_FIELDS = "name,cca3,population,flags,languages,capital,region,subregion,currencies,area,timezones"
    }

    /**
     * Obtiene la lista completa de todos los países.
     * Endpoint: GET https://restcountries.com/v3.1/all?fields=name,flags,cca3
     *
     * @param fields Campos a incluir en la respuesta
     * @return Lista de [CountryDto] con todos los países disponibles
     */
    @GET("all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = LIST_FIELDS
    ): List<CountryDto>

    /**
     * Obtiene un país específico por su código alfa-3 (cca3).
     * Endpoint: GET https://restcountries.com/v3.1/alpha/{code}?fields=...
     *
     * @param code Código alfa-3 del país (ej: "USA", "ESP", "MEX")
     * @param fields Campos a incluir en la respuesta
     * @return [CountryDto] con los datos del país (la API devuelve un objeto cuando se usan fields)
     */
    @GET("alpha/{code}")
    suspend fun getCountryByCode(
        @Path("code") code: String,
        @Query("fields") fields: String = DETAIL_FIELDS
    ): CountryDto
}

