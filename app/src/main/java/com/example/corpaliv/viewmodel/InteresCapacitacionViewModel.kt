package com.example.corpaliv.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpaliv.data.AppDatabase
import com.example.corpaliv.data.SolicitudRepository
import com.example.corpaliv.model.Solicitud
import com.example.corpaliv.model.InteresCapacitacionErrores
import com.example.corpaliv.model.InteresCapacitacionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

// ViewModel que administra el estado y la validación del formulario.
class InteresCapacitacionViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = SolicitudRepository(
        AppDatabase.getInstance(application).solicitudDao()
    )

    // El estado privado se modifica aquí y la interfaz observa el estado público.
    private val _estado = MutableStateFlow(InteresCapacitacionUiState())
    val estado: StateFlow<InteresCapacitacionUiState> = _estado.asStateFlow()

    fun nuevoFormulario() {
        _estado.value = InteresCapacitacionUiState()
    }

    fun cargarSolicitud(solicitud: Solicitud) {
        _estado.value = InteresCapacitacionUiState(
            solicitudOriginal = solicitud,
            nombre = solicitud.nombre,
            correo = solicitud.correo,
            cantidadParticipantes = solicitud.cantidad?.toString() ?: "",
            fechaPreferidaMillis = solicitud.fechaPreferida,
            autorizaContacto = true,
            recibirNovedades = solicitud.recibirNovedades
        )
    }

    // Actualiza el nombre y limpia el error asociado.
    fun onNombreChange(valor: String) {
        _estado.update { estadoActual ->
            estadoActual.copy(
                nombre = valor,
                errores = estadoActual.errores.copy(nombre = null)
            )
        }
    }

    // Actualiza el correo y limpia el error asociado.
    fun onCorreoChange(valor: String) {
        _estado.update { estadoActual ->
            estadoActual.copy(
                correo = valor,
                errores = estadoActual.errores.copy(correo = null)
            )
        }
    }

    // El campo se escribe como texto y se convierte a número al validarlo.
    fun onCantidadParticipantesChange(valor: String) {
        _estado.update { estadoActual ->
            estadoActual.copy(
                cantidadParticipantes = valor,
                errores = estadoActual.errores.copy(
                    cantidadParticipantes = null
                )
            )
        }
    }

    // Guarda la fecha elegida en milisegundos.
    fun onFechaPreferidaChange(valor: Long?) {
        _estado.update { estadoActual ->
            estadoActual.copy(
                fechaPreferidaMillis = valor,
                errores = estadoActual.errores.copy(fechaPreferida = null)
            )
        }
    }

    // Actualiza la autorización de contacto.
    fun onAutorizaContactoChange(valor: Boolean) {
        _estado.update { estadoActual ->
            estadoActual.copy(
                autorizaContacto = valor,
                errores = estadoActual.errores.copy(autorizaContacto = null)
            )
        }
    }

    // Actualiza la preferencia para recibir novedades.
    fun onRecibirNovedadesChange(valor: Boolean) {
        _estado.update { estadoActual ->
            estadoActual.copy(recibirNovedades = valor)
        }
    }

    // Revisa todos los campos y publica sus errores en el estado.
    fun validarFormulario(): Boolean {
        val datos = _estado.value
        val cantidad = datos.cantidadParticipantes.toIntOrNull()

        val errores = InteresCapacitacionErrores(
            nombre = when {
                datos.nombre.isBlank() -> "El nombre es obligatorio"
                datos.nombre.trim().length < 3 -> "Ingresa al menos 3 caracteres"
                else -> null
            },
            correo = when {
                datos.correo.isBlank() -> "El correo es obligatorio"
                !Patterns.EMAIL_ADDRESS.matcher(datos.correo.trim()).matches() ->
                    "El correo no es válido"
                else -> null
            },
            cantidadParticipantes = when {
                datos.cantidadParticipantes.isBlank() ->
                    "Indica la cantidad de participantes"

                cantidad == null ->
                    "Ingresa una cantidad válida"

                cantidad <= 0 ->
                    "La cantidad debe ser mayor que cero"

                else -> null
            },
            fechaPreferida = when {
                datos.fechaPreferidaMillis == null -> "Selecciona una fecha preferida"
                Instant.ofEpochMilli(datos.fechaPreferidaMillis)
                    .atZone(ZoneOffset.UTC).toLocalDate().isBefore(LocalDate.now()) ->
                    "La fecha no puede ser anterior a hoy"
                else -> null
            },
            autorizaContacto = if (!datos.autorizaContacto) {
                "Acepta guardar los datos en la aplicación"
            } else {
                null
            }
        )

        _estado.update { estadoActual ->
            estadoActual.copy(errores = errores)
        }

        return errores == InteresCapacitacionErrores()
    }

    fun guardarSolicitud(onGuardado: () -> Unit) {
        if (_estado.value.guardando) return
        if (!validarFormulario()) return
        _estado.update { it.copy(guardando = true, errorGuardado = null) }

        viewModelScope.launch {
            try {
                val datos = _estado.value
                if (datos.solicitudOriginal == null) {
                    repositorio.guardar(Solicitud(
                        tipo = "Capacitación",
                        nombre = datos.nombre.trim(),
                        correo = datos.correo.trim(),
                        cantidad = datos.cantidadParticipantes.toInt(),
                        fechaPreferida = datos.fechaPreferidaMillis,
                        recibirNovedades = datos.recibirNovedades
                    ))
                } else {
                    repositorio.actualizar(datos.solicitudOriginal.copy(
                        nombre = datos.nombre.trim(),
                        correo = datos.correo.trim(),
                        cantidad = datos.cantidadParticipantes.toInt(),
                        fechaPreferida = datos.fechaPreferidaMillis,
                        recibirNovedades = datos.recibirNovedades
                    ))
                }
            } catch (e: Exception) {
                _estado.update {
                    it.copy(guardando = false,
                        errorGuardado = "No se pudo guardar. Inténtalo nuevamente.")
                }
                return@launch
            }
            onGuardado()
        }
    }
}
