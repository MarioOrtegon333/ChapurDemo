package com.efisense.chapurdemo.presentation.countrylist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.efisense.chapurdemo.domain.model.Country
import com.efisense.chapurdemo.presentation.components.ErrorScreen
import com.efisense.chapurdemo.presentation.components.ShimmerCountryList
import org.koin.androidx.compose.koinViewModel

/**
 * Pantalla principal que muestra el listado de países.
 * Utiliza LazyColumn para renderizar la lista de forma eficiente.
 * Incluye una barra de búsqueda dinámica que consulta la API en tiempo real.
 *
 * @param onCountryClick Callback que se ejecuta cuando se selecciona un país
 * @param viewModel ViewModel inyectado por Koin
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(
    onCountryClick: (String) -> Unit,
    viewModel: CountryListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Países del Mundo",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onClearClick = viewModel::clearSearch,
                isSearching = isSearching,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when (val state = uiState) {
                is CountryListUiState.Loading -> {
                    ShimmerCountryList()
                }

                is CountryListUiState.Success -> {
                    if (state.countries.isEmpty()) {
                        EmptySearchResult(
                            query = searchQuery,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        CountryList(
                            countries = state.countries,
                            onCountryClick = onCountryClick
                        )
                    }
                }

                is CountryListUiState.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onRetry = viewModel::retry
                    )
                }
            }
        }
    }
}

/**
 * Barra de búsqueda para filtrar países.
 *
 * @param query Texto de búsqueda actual
 * @param onQueryChange Callback cuando cambia el texto
 * @param onClearClick Callback cuando se limpia la búsqueda
 * @param isSearching Indica si se está realizando una búsqueda
 * @param modifier Modificador opcional
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    isSearching: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = {
            Text(text = "Buscar país...")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty() || isSearching,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar búsqueda",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    )
}

/**
 * Pantalla que muestra cuando no hay resultados de búsqueda.
 *
 * @param query Texto de búsqueda actual
 * @param modifier Modificador opcional
 */
@Composable
private fun EmptySearchResult(
    query: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔍",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (query.isNotEmpty()) {
                    "No se encontraron países para \"$query\""
                } else {
                    "No hay países para mostrar"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

/**
 * Lista de países utilizando LazyColumn para renderizado eficiente.
 *
 * @param countries Lista de países a mostrar
 * @param onCountryClick Callback que se ejecuta al seleccionar un país
 * @param modifier Modificador opcional
 */
@Composable
private fun CountryList(
    countries: List<Country>,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = countries,
            key = { it.code }
        ) { country ->
            CountryListItem(
                country = country,
                onClick = { onCountryClick(country.code) }
            )
        }
    }
}

/**
 * Elemento individual de la lista que muestra la información básica de un país.
 *
 * @param country El país a mostrar
 * @param onClick Callback que se ejecuta al hacer clic en el elemento
 * @param modifier Modificador opcional
 */
@Composable
private fun CountryListItem(
    country: Country,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bandera del país usando Coil
            AsyncImage(
                model = country.flagUrl,
                contentDescription = "Bandera de ${country.commonName}",
                modifier = Modifier
                    .size(60.dp, 40.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = country.commonName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = country.capital,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
