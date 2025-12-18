# ChapurDemo 🌍

Aplicación Android para explorar información de países del mundo utilizando la API REST Countries.

---

## 📐 Arquitectura

El proyecto implementa **Clean Architecture** con el patrón **MVVM**:

```
┌─────────────────────────────────────────────────────┐
│              PRESENTATION LAYER                     │
│   Jetpack Compose + ViewModel + StateFlow           │
└─────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│                DOMAIN LAYER                         │
│   UseCases + Models + Repository Interfaces         │
└─────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│                 DATA LAYER                          │
│   Repository Impl + Remote DataSource + Mappers     │
└─────────────────────────────────────────────────────┘
```

### Decisiones de Diseño

| Decisión | Justificación |
|----------|---------------|
| **Clean Architecture** | Separación de responsabilidades, testabilidad y mantenibilidad |
| **MVVM + StateFlow** | Flujo unidireccional de datos y manejo reactivo del estado |
| **UseCases** | Encapsulan lógica de negocio específica, un caso de uso por acción |
| **Repository Pattern** | Abstrae el origen de datos, facilita testing y futura implementación de cache |
| **Koin** | Inyección de dependencias ligera, ideal para proyectos Kotlin |
| **Jetpack Compose** | UI declarativa moderna con menos código boilerplate |

### Estructura del Proyecto

```
app/src/main/java/com/efisense/chapurdemo/
├── di/                     # Módulos de inyección de dependencias
│   └── AppModules.kt
├── domain/                 # Capa de dominio (reglas de negocio)
│   ├── model/
│   ├── repository/
│   └── usecase/
├── data/                   # Capa de datos
│   ├── remote/
│   ├── mapper/
│   └── repository/
└── presentation/           # Capa de presentación
    ├── countrylist/
    ├── countrydetail/
    ├── components/
    ├── navigation/
    └── theme/
```

---

## 🚀 Ejecutar el Proyecto

### Requisitos

- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK 11**
- **Gradle 8.x**
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 35

### Pasos

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/ChapurDemo.git
   cd ChapurDemo
   ```

2. **Abrir en Android Studio** y esperar sincronización de Gradle

---

## 📦 Generar APK

### APK Debug
```bash
./gradlew assembleDebug
```
**Ubicación:** `app/build/outputs/apk/debug/app-debug.apk`

### APK Release
```bash
./gradlew assembleRelease
```
**Ubicación:** `app/build/outputs/apk/release/app-release-unsigned.apk`

> ⚠️ Para release firmado, configurar `signingConfigs` en `build.gradle.kts`

---

## 📚 Librerías de Terceros

| Librería | Versión | Propósito |
|----------|---------|-----------|
| **Jetpack Compose BOM** | 2024.12.01 | UI declarativa moderna y unificada |
| **Navigation Compose** | 2.8.5 | Navegación type-safe entre pantallas |
| **Lifecycle ViewModel** | 2.8.7 | Gestión del ciclo de vida y estado UI |
| **Retrofit** | 2.11.0 | Cliente HTTP para consumo de APIs REST |
| **OkHttp** | 4.12.0 | Cliente HTTP con interceptores para logging |
| **Gson** | 2.11.0 | Serialización/deserialización JSON |
| **Koin** | 3.5.6 | Inyección de dependencias ligera para Kotlin |
| **Coil** | 2.7.0 | Carga eficiente de imágenes con soporte Compose |

### ¿Por qué estas librerías?

- **Retrofit + OkHttp**: Estándar de la industria para networking en Android, tipado fuerte y fácil manejo de errores
- **Koin**: Más ligero que Dagger/Hilt, sintaxis DSL Kotlin-friendly, sin generación de código
- **Coil**: Diseñado para Kotlin y Coroutines, integración nativa con Compose
- **Compose Navigation**: Navegación declarativa coherente con el paradigma de Compose

---

## 🌐 API Utilizada

[REST Countries API v3.1](https://restcountries.com/)

```
Base URL: https://restcountries.com/v3.1/
Endpoints:
  GET /all              → Lista todos los países
  GET /alpha/{code}     → País por código alfa-3
  GET /name/{name}      → Buscar por nombre
```

---

## 📄 Licencia

```
MIT License

Copyright (c) 2024 ChapurDemo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software.
```

