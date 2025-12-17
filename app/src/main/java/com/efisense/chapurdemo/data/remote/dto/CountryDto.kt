package com.efisense.chapurdemo.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO (Data Transfer Object) que representa la respuesta de la API REST Countries.
 * Mapea los campos JSON de la respuesta a propiedades Kotlin.
 */
data class CountryDto(
    @SerializedName("name")
    val name: NameDto?,

    @SerializedName("cca3")
    val cca3: String?,

    @SerializedName("population")
    val population: Long?,

    @SerializedName("flags")
    val flags: FlagsDto?,

    @SerializedName("languages")
    val languages: Map<String, String>?,

    @SerializedName("capital")
    val capital: List<String>?,

    @SerializedName("region")
    val region: String?,

    @SerializedName("subregion")
    val subregion: String?,

    @SerializedName("currencies")
    val currencies: Map<String, CurrencyDto>?,

    @SerializedName("area")
    val area: Double?,

    @SerializedName("timezones")
    val timezones: List<String>?
)

data class NameDto(
    @SerializedName("common")
    val common: String?,

    @SerializedName("official")
    val official: String?
)

data class FlagsDto(
    @SerializedName("png")
    val png: String?,

    @SerializedName("svg")
    val svg: String?,

    @SerializedName("alt")
    val alt: String?
)

data class CurrencyDto(
    @SerializedName("name")
    val name: String?,

    @SerializedName("symbol")
    val symbol: String?
)
