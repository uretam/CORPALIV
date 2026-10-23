package com.example.corpaliv.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpaliv.data.AppDatabase
import com.example.corpaliv.data.SolicitudRepository
import com.example.corpaliv.model.Solicitud
import com.example.corpaliv.model.SolicitudColaboracionErrores
import com.example.corpaliv.model.SolicitudColaboracionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel adaptado para administrar y guardar la solicitud.
class SolicitudColaboracionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = SolicitudRepository(
        AppDatabase.getInstance(application).solicitudDao()
    )

    // Estado privado modificable y estado público observable por la interfaz.
    private val _estado =
        MutableStateFlow(SolicitudColaboracionUiState())
    val estado: StateFlow<SolicitudColaboracionUiState> =
        _estado.asStateFlow()

    fun nuevoFormulario() {
        _estado.value = SolicitudColaboracionUiState()
    }

    fun cargarSolicitud(solicitud: Solicitud) {
        _estado.value = SolicitudColaboracionUiState(
            solicitudOriginal = solicitud,
            nombre = solicitud.nombre,
            correo = solicitud.correo,
            tipoColaboracion = solicitud.modalidad,
            mensaje = solicitud.mensaje
        )
    }

    // Cada cambio actualiza el campo y limpia el error de ese campo.
    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onTipoColaboracionChange(valor: String) {
        _estado.update {
            it.copy(
                tipoColaboracion = valor,
                errores = it.errores.copy(tipoColaboracion = null)
            )
        }
    }

    fun onMensajeChange(valor: String) {
        _estado.update {
            it.copy(
                mensaje = valor,
                errores = it.errores.copy(mensaje = null)
            )
        }
    }

    // Revisa los campos y guarda un mensaje de error por cada dato inválido.
    fun validarFormulario(): Boolean {
        val datos = _estado.value

        val errores = SolicitudColaboracionErrores(
            nombre = when {
                datos.nombre.isBlank() ->
                    "El nombre es obligatorio"

                datos.nombre.trim().length < 3 ->
                    "Ingresa al menos 3 caracteres"

                else -> null
            },
            correo = when {
                datos.correo.isBlank() ->
                    "El correo es obligatorio"

                !Patterns.EMAIL_ADDRESS.matcher(datos.correo.trim()).matches() ->
                    "El correo no es válido"

                else -> null
            },
            tipoColaboracion = if (
                datos.tipoColaboracion.isBlank()
            ) {
                "Selecciona una forma de colaborar"
            } else {
                null
            },
            mensaje = when {
                datos.mensaje.isBlank() ->
                    "El mensaje es obligatorio"

                datos.mensaje.trim().length < 5 ->
                    "Ingresa al menos 5 caracteres"

                else -> null
            }
        )

        _estado.update {
            it.copy(errores = errores)
        }

        return errores == SolicitudColaboracionErrores()
    }

    // La validación ocurre aquí, antes de crear o actualizar una fila en Room.
    fun guardarSolicitud(onGuardado: () -> Unit) {
        if (_estado.value.guardando) return
        if (!validarFormulario()) return
        _estado.update { it.copy(guardando = true, errorGuardado = null) }

        viewModelScope.launch {
            try {
                val datos = _estado.value
                if (datos.solicitudOriginal == null) {
                    repositorio.guardar(Solicitud(
                        tipo = "Colaboración",
                        nombre = datos.nombre.trim(),
                        correo = datos.correo.trim(),
                        modalidad = datos.tipoColaboracion,
                        mensaje = datos.mensaje.trim()
                    ))
                } else {
                    repositorio.actualizar(datos.solicitudOriginal.copy(
                        nombre = datos.nombre.trim(),
                        correo = datos.correo.trim(),
                        modalidad = datos.tipoColaboracion,
                        mensaje = datos.mensaje.trim()
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
