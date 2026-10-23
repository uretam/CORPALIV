package com.example.corpaliv.model

// Estado que captura los datos del formulario de interés en una capacitación.
data class InteresCapacitacionUiState(
    val solicitudOriginal: Solicitud? = null,
    val nombre: String = "",
    val correo: String = "",

    // Compose edita el contenido del campo como texto;
    // el ViewModel validará que represente un número entero.
    val cantidadParticipantes: String = "",

    // La fecha se guarda como milisegundos desde el selector de fecha.
    val fechaPreferidaMillis: Long? = null,

    val autorizaContacto: Boolean = false,
    val recibirNovedades: Boolean = false,
    val guardando: Boolean = false,
    val errores: InteresCapacitacionErrores =
        InteresCapacitacionErrores(),
    val errorGuardado: String? = null
)

// Mensajes de validación del formulario.
data class InteresCapacitacionErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val cantidadParticipantes: String? = null,
    val fechaPreferida: String? = null,
    val autorizaContacto: String? = null
)
