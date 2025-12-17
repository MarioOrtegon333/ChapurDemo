package com.efisense.chapurdemo.domain.model

/**
 * Modelo de dominio que representa un país.
 * Esta clase es independiente de la capa de datos y contiene
 * solo la información relevante para la lógica de negocio y presentación.
 */
data class Country(
    val code: String,
    val commonName: String,
    val officialName: String,
    val population: Long,
    val flagUrl: String,
    val languages: List<String>,
    val capital: String,
    val region: String,
    val subregion: String,
    val currencies: List<Currency>,
    val area: Double,
    val timezones: List<String>
)

/**
 * Modelo de dominio que representa una moneda de un país.
 */
data class Currency(
    val name: String,
    val symbol: String
)
