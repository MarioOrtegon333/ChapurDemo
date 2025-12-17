package com.efisense.chapurdemo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.efisense.chapurdemo.presentation.countrydetail.CountryDetailScreen
import com.efisense.chapurdemo.presentation.countrylist.CountryListScreen

/**
 * Composable que define el grafo de navegación de la aplicación.
 * Utiliza Jetpack Navigation Compose para manejar la navegación entre pantallas.
 *
 * @param navController Controlador de navegación
 * @param modifier Modificador opcional
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CountryList.route,
        modifier = modifier
    ) {
        // Pantalla de listado de países
        composable(route = Screen.CountryList.route) {
            CountryListScreen(
                onCountryClick = { countryCode ->
                    navController.navigate(Screen.CountryDetail.createRoute(countryCode))
                }
            )
        }

        // Pantalla de detalle de país
        composable(
            route = Screen.CountryDetail.route,
            arguments = listOf(
                navArgument("countryCode") {
                    type = NavType.StringType
                }
            )
        ) {
            CountryDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

