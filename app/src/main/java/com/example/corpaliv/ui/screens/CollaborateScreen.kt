package com.example.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpaliv.ui.components.ErrorIcon
import com.example.corpaliv.ui.components.FormaColaboracionMenu
import com.example.corpaliv.ui.components.PageIntro
import com.example.corpaliv.viewmodel.SolicitudColaboracionViewModel

// Formulario para registrar una intención de colaboración en el dispositivo.
@Composable
fun CollaborateScreen(
    modifier: Modifier = Modifier,
    formularioViewModel: SolicitudColaboracionViewModel = viewModel(),
    onFormularioValido: () -> Unit
) {
    // Observa el estado del formulario en el ViewModel.
    val estado by formularioViewModel.estado.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        PageIntro("Registra tu interés",
            "Cuéntanos cómo te gustaría colaborar. Podrás revisar este registro más adelante.")
        Text("Tus datos", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = {
                formularioViewModel.onNombreChange(it)
            },
            label = { Text("Empresa o persona de contacto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = estado.errores.nombre != null,
            trailingIcon = { if (estado.errores.nombre != null) ErrorIcon() },
            supportingText = {
                estado.errores.nombre?.let { Text(it) }
            }
        )

        OutlinedTextField(
            value = estado.correo,
            onValueChange = {
                formularioViewModel.onCorreoChange(it)
            },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = estado.errores.correo != null,
            trailingIcon = { if (estado.errores.correo != null) ErrorIcon() },
            supportingText = {
                estado.errores.correo?.let { Text(it) }
            }
        )

        Text("Tu interés", style = MaterialTheme.typography.titleLarge)
        // La opción se elige desde el menú adaptado del ejemplo de clase.
        FormaColaboracionMenu(
            valorSeleccionado = estado.tipoColaboracion,
            errorMensaje = estado.errores.tipoColaboracion,
            onSeleccionar = { opcion ->
                formularioViewModel.onTipoColaboracionChange(opcion)
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.mensaje,
            onValueChange = {
                formularioViewModel.onMensajeChange(it)
            },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            isError = estado.errores.mensaje != null,
            trailingIcon = { if (estado.errores.mensaje != null) ErrorIcon() },
            supportingText = {
                estado.errores.mensaje?.let { Text(it) }
            }
        )

        Button(
            onClick = {
                formularioViewModel.guardarSolicitud(onFormularioValido)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !estado.guardando
        ) {
            Text(when {
                estado.guardando -> "Guardando..."
                estado.solicitudOriginal == null -> "Registrar interés"
                else -> "Guardar cambios"
            })
        }

        estado.errorGuardado?.let { mensaje ->
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}
