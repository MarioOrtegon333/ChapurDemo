package com.efisense.chapurdemo.data.remote

import com.efisense.chapurdemo.data.remote.api.CountryApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Objeto que proporciona la configuración de red.
 * Contiene la URL base y métodos para crear instancias de Retrofit y OkHttpClient.
 */
object NetworkModule {

    const val BASE_URL = "https://restcountries.com/v3.1/"

    /**
     * Crea y configura una instancia de OkHttpClient.
     * Incluye un interceptor de logging para depuración y timeouts configurados.
     */
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Crea y configura una instancia de Retrofit.
     * Usa Gson como convertidor JSON y el OkHttpClient configurado.
     */
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Crea la instancia del servicio API de países.
     */
    fun provideCountryApiService(retrofit: Retrofit): CountryApiService {
        return retrofit.create(CountryApiService::class.java)
    }
}

