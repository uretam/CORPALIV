package com.example.corpaliv.model

// Estado del formulario de colaboración.
data class SolicitudColaboracionUiState(
    val solicitudOriginal: Solicitud? = null,
    val nombre: String = "",
    val correo: String = "",
    val tipoColaboracion: String = "",
    val mensaje: String = "",
    val guardando: Boolean = false,
    val errores: SolicitudColaboracionErrores = SolicitudColaboracionErrores(),
    val errorGuardado: String? = null
)

// Errores de validación separados por campo.
data class SolicitudColaboracionErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val tipoColaboracion: String? = null,
    val mensaje: String? = null
)
