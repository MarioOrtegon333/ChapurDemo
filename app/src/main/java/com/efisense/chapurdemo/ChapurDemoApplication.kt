package com.efisense.chapurdemo

import android.app.Application
import com.efisense.chapurdemo.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Clase Application personalizada para la aplicación ChapurDemo.
 * Inicializa Koin para la inyección de dependencias al inicio de la aplicación.
 */
class ChapurDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializar Koin
        startKoin {
            // Usar el logger de Android para depuración
            androidLogger(Level.DEBUG)

            // Proporcionar el contexto de Android
            androidContext(this@ChapurDemoApplication)

            // Cargar todos los módulos de la aplicación
            modules(appModules)
        }
    }
}

