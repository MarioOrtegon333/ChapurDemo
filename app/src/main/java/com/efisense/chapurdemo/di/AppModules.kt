package com.efisense.chapurdemo.di

import com.efisense.chapurdemo.data.remote.NetworkModule
import com.efisense.chapurdemo.data.repository.CountryRepositoryImpl
import com.efisense.chapurdemo.domain.repository.CountryRepository
import com.efisense.chapurdemo.domain.usecase.GetAllCountriesUseCase
import com.efisense.chapurdemo.domain.usecase.GetCountryByCodeUseCase
import com.efisense.chapurdemo.domain.usecase.SearchCountriesUseCase
import com.efisense.chapurdemo.presentation.countrydetail.CountryDetailViewModel
import com.efisense.chapurdemo.presentation.countrylist.CountryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Módulo de Koin para la capa de red.
 * Proporciona las instancias de OkHttpClient, Retrofit y el servicio API.
 */
val networkModule = module {
    // OkHttpClient
    single { NetworkModule.provideOkHttpClient() }

    // Retrofit
    single { NetworkModule.provideRetrofit(get()) }

    // API Service
    single { NetworkModule.provideCountryApiService(get()) }
}

/**
 * Módulo de Koin para la capa de datos.
 * Proporciona la implementación del repositorio.
 */
val dataModule = module {
    // Repository
    single<CountryRepository> { CountryRepositoryImpl(get()) }
}

/**
 * Módulo de Koin para la capa de dominio.
 * Proporciona los casos de uso.
 */
val domainModule = module {
    // Use Cases
    factory { GetAllCountriesUseCase(get()) }
    factory { GetCountryByCodeUseCase(get()) }
    factory { SearchCountriesUseCase(get()) }
}

/**
 * Módulo de Koin para la capa de presentación.
 * Proporciona los ViewModels.
 */
val presentationModule = module {
    // ViewModels
    viewModel { CountryListViewModel(get(), get()) }
    viewModel { CountryDetailViewModel(get(), get()) }
}

/**
 * Lista de todos los módulos de Koin de la aplicación.
 * Se utiliza para inicializar Koin en la Application.
 */
val appModules = listOf(
    networkModule,
    dataModule,
    domainModule,
    presentationModule
)

