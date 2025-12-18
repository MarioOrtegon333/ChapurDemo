package com.efisense.chapurdemo.presentation.countrylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efisense.chapurdemo.domain.usecase.GetAllCountriesUseCase
import com.efisense.chapurdemo.domain.usecase.SearchCountriesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de listado de países.
 * Sigue el patrón MVVM y gestiona el estado de la UI de forma reactiva usando StateFlow.
 * Incluye funcionalidad de búsqueda dinámica con debounce.
 *
 * @param getAllCountriesUseCase Caso de uso para obtener todos los países
 * @param searchCountriesUseCase Caso de uso para buscar países por nombre
 */
@OptIn(FlowPreview::class)
class CountryListViewModel(
    private val getAllCountriesUseCase: GetAllCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    /**
     * Estado mutable privado de la UI.
     * Solo el ViewModel puede modificar este estado.
     */
    private val _uiState = MutableStateFlow<CountryListUiState>(CountryListUiState.Loading)

    /**
     * Estado público de la UI como StateFlow inmutable.
     * La UI observa este Flow para reaccionar a cambios de estado.
     */
    val uiState: StateFlow<CountryListUiState> = _uiState.asStateFlow()

    /**
     * Estado mutable privado del texto de búsqueda.
     */
    private val _searchQuery = MutableStateFlow("")

    /**
     * Estado público del texto de búsqueda.
     */
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /**
     * Indica si se está realizando una búsqueda (para mostrar indicador de carga en el campo de búsqueda).
     */
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    /**
     * Página actual de la paginación.
     */
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    /**
     * Lista completa de países (sin paginar) para realizar la paginación local.
     */
    private var allCountries: List<com.efisense.chapurdemo.domain.model.Country> = emptyList()

    init {
        // Cargar países automáticamente al inicializar el ViewModel
        loadCountries()

        // Configurar el flujo de búsqueda con debounce
        setupSearchFlow()
    }

    /**
     * Configura el flujo de búsqueda con debounce para evitar demasiadas llamadas a la API.
     */
    private fun setupSearchFlow() {
        _searchQuery
            .debounce(300) // Esperar 300ms después de que el usuario deje de escribir
            .distinctUntilChanged() // Solo procesar si el valor cambió
            .onEach { query ->
                if (query.isBlank()) {
                    loadCountries()
                } else {
                    searchCountries(query)
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Actualiza el texto de búsqueda.
     * El flujo configurado en setupSearchFlow se encarga de ejecutar la búsqueda con debounce.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * Limpia el texto de búsqueda y muestra todos los países.
     */
    fun clearSearch() {
        _searchQuery.value = ""
        _currentPage.value = 1
    }

    /**
     * Carga la lista de países desde la API.
     * Actualiza el estado de la UI según el resultado obtenido.
     */
    fun loadCountries() {
        viewModelScope.launch {
            _uiState.value = CountryListUiState.Loading
            _isSearching.value = false

            getAllCountriesUseCase().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { countries ->
                        allCountries = countries
                        _currentPage.value = 1
                        getPaginatedState(countries, 1)
                    },
                    onFailure = { exception ->
                        CountryListUiState.Error(
                            exception.message ?: "Error desconocido al cargar los países"
                        )
                    }
                )
            }
        }
    }

    /**
     * Obtiene el estado paginado de la lista de países.
     */
    private fun getPaginatedState(countries: List<com.efisense.chapurdemo.domain.model.Country>, page: Int): CountryListUiState.Success {
        val totalPages = (countries.size + PAGE_SIZE - 1) / PAGE_SIZE
        val startIndex = (page - 1) * PAGE_SIZE
        val endIndex = minOf(startIndex + PAGE_SIZE, countries.size)
        val paginatedList = if (startIndex < countries.size) {
            countries.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
        return CountryListUiState.Success(
            countries = paginatedList,
            currentPage = page,
            totalPages = totalPages,
            totalCountries = countries.size
        )
    }

    /**
     * Navega a la página siguiente.
     */
    fun nextPage() {
        val currentState = _uiState.value
        if (currentState is CountryListUiState.Success && currentState.currentPage < currentState.totalPages) {
            val newPage = currentState.currentPage + 1
            _currentPage.value = newPage
            _uiState.value = getPaginatedState(allCountries, newPage)
        }
    }

    /**
     * Navega a la página anterior.
     */
    fun previousPage() {
        val currentState = _uiState.value
        if (currentState is CountryListUiState.Success && currentState.currentPage > 1) {
            val newPage = currentState.currentPage - 1
            _currentPage.value = newPage
            _uiState.value = getPaginatedState(allCountries, newPage)
        }
    }

    /**
     * Navega a una página específica.
     */
    fun goToPage(page: Int) {
        val currentState = _uiState.value
        if (currentState is CountryListUiState.Success && page in 1..currentState.totalPages) {
            _currentPage.value = page
            _uiState.value = getPaginatedState(allCountries, page)
        }
    }

    /**
     * Busca países por nombre usando la API.
     */
    private fun searchCountries(query: String) {
        viewModelScope.launch {
            _isSearching.value = true

            searchCountriesUseCase(query).collect { result ->
                _isSearching.value = false
                _uiState.value = result.fold(
                    onSuccess = { countries ->
                        allCountries = countries
                        _currentPage.value = 1
                        if (countries.isEmpty()) {
                            CountryListUiState.Success(emptyList(), 1, 1, 0)
                        } else {
                            getPaginatedState(countries, 1)
                        }
                    },
                    onFailure = { exception ->
                        // Si no se encontraron países (404), mostrar lista vacía
                        if (exception.message?.contains("No se encontraron") == true) {
                            allCountries = emptyList()
                            CountryListUiState.Success(emptyList(), 1, 1, 0)
                        } else {
                            CountryListUiState.Error(
                                exception.message ?: "Error al buscar países"
                            )
                        }
                    }
                )
            }
        }
    }

    /**
     * Reintenta cargar la lista de países.
     * Se usa cuando el usuario quiere reintentar después de un error.
     */
    fun retry() {
        if (_searchQuery.value.isBlank()) {
            loadCountries()
        } else {
            searchCountries(_searchQuery.value)
        }
    }
}

