package com.efisense.chapurdemo.presentation.countrydetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.efisense.chapurdemo.domain.model.Country
import com.efisense.chapurdemo.presentation.components.ErrorScreen
import com.efisense.chapurdemo.presentation.components.ShimmerCountryDetail
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla de detalle que muestra información completa de un país.
 *
 * @param onBackClick Callback que se ejecuta cuando se presiona el botón de retroceso
 * @param viewModel ViewModel inyectado por Koin
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CountryDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (val state = uiState) {
                            is CountryDetailUiState.Success -> state.country.commonName
                            else -> "Detalle del País"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is CountryDetailUiState.Loading -> {
                ShimmerCountryDetail(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is CountryDetailUiState.Success -> {
                CountryDetailContent(
                    country = state.country,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is CountryDetailUiState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = viewModel::retry,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Contenido principal del detalle del país.
 *
 * @param country El país a mostrar
 * @param modifier Modificador opcional
 */
@Composable
private fun CountryDetailContent(
    country: Country,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bandera grande
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            AsyncImage(
                model = country.flagUrl,
                contentDescription = "Bandera de ${country.commonName}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre oficial
        Text(
            text = country.officialName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card con información detallada
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Población
                DetailRow(
                    label = "Población",
                    value = numberFormat.format(country.population)
                )

                HorizontalDivider()

                // Idiomas
                DetailRow(
                    label = "Idioma(s)",
                    value = if (country.languages.isNotEmpty()) {
                        country.languages.joinToString(", ")
                    } else {
                        "No disponible"
                    }
                )

                HorizontalDivider()

                // Capital
                DetailRow(
                    label = "Capital",
                    value = country.capital
                )

                HorizontalDivider()

                // Región
                DetailRow(
                    label = "Región",
                    value = country.region
                )

                HorizontalDivider()

                // Subregión
                DetailRow(
                    label = "Subregión",
                    value = country.subregion
                )

                HorizontalDivider()

                // Área
                DetailRow(
                    label = "Área",
                    value = "${numberFormat.format(country.area)} km²"
                )

                HorizontalDivider()

                // Monedas
                DetailRow(
                    label = "Moneda(s)",
                    value = if (country.currencies.isNotEmpty()) {
                        country.currencies.joinToString(", ") {
                            "${it.name} (${it.symbol})"
                        }
                    } else {
                        "No disponible"
                    }
                )

                HorizontalDivider()

                // Zonas horarias
                DetailRow(
                    label = "Zona(s) horaria(s)",
                    value = if (country.timezones.isNotEmpty()) {
                        country.timezones.joinToString(", ")
                    } else {
                        "No disponible"
                    }
                )
            }
        }
    }
}

/**
 * Fila de detalle que muestra una etiqueta y su valor.
 *
 * @param label Etiqueta descriptiva
 * @param value Valor a mostrar
 * @param modifier Modificador opcional
 */
@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.6f)
        )
    }
}

