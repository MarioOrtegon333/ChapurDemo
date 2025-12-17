package com.efisense.chapurdemo.data.mapper

import com.efisense.chapurdemo.data.remote.dto.CountryDto
import com.efisense.chapurdemo.domain.model.Country
import com.efisense.chapurdemo.domain.model.Currency

/**
 * Objeto que contiene funciones para mapear DTOs a modelos de dominio.
 * Proporciona una conversión limpia entre la capa de datos y la capa de dominio.
 */
object CountryMapper {

    /**
     * Convierte un [CountryDto] (objeto de datos de la API) a un [Country] (modelo de dominio).
     * Maneja valores nulos proporcionando valores por defecto cuando es necesario.
     *
     * @param dto El DTO recibido de la API
     * @return El modelo de dominio [Country]
     */
    fun fromDto(dto: CountryDto): Country {
        return Country(
            code = dto.cca3 ?: "",
            commonName = dto.name?.common ?: "Unknown",
            officialName = dto.name?.official ?: "Unknown",
            population = dto.population ?: 0L,
            flagUrl = dto.flags?.png ?: "",
            languages = dto.languages?.values?.toList() ?: emptyList(),
            capital = dto.capital?.firstOrNull() ?: "N/A",
            region = dto.region ?: "N/A",
            subregion = dto.subregion ?: "N/A",
            currencies = dto.currencies?.map { (_, currencyDto) ->
                Currency(
                    name = currencyDto.name ?: "Unknown",
                    symbol = currencyDto.symbol ?: ""
                )
            } ?: emptyList(),
            area = dto.area ?: 0.0,
            timezones = dto.timezones ?: emptyList()
        )
    }

    /**
     * Convierte una lista de [CountryDto] a una lista de [Country].
     *
     * @param dtoList La lista de DTOs recibidos de la API
     * @return Lista de modelos de dominio [Country]
     */
    fun fromDtoList(dtoList: List<CountryDto>): List<Country> {
        return dtoList.map { fromDto(it) }
    }
}

