package com.efisense.chapurdemo.presentation.navigation

/**
 * Objeto sellado que define las rutas de navegación de la aplicación.
 * Centraliza todas las rutas para evitar errores de tipeo y facilitar el mantenimiento.
 */
sealed class Screen(val route: String) {

    /**
     * Pantalla de listado de países.
     * Ruta: "country_list"
     */
    data object CountryList : Screen("country_list")

    /**
     * Pantalla de detalle de país.
     * Ruta: "country_detail/{countryCode}"
     *
     * @param countryCode Código alfa-3 del país (argumento de navegación)
     */
    data object CountryDetail : Screen("country_detail/{countryCode}") {
        /**
         * Crea la ruta completa con el código del país.
         *
         * @param countryCode Código alfa-3 del país
         * @return Ruta formateada con el código del país
         */
        fun createRoute(countryCode: String): String {
            return "country_detail/$countryCode"
        }
    }
}
