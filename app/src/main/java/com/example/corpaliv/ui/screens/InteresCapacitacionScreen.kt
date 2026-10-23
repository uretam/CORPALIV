package com.example.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpaliv.ui.components.ErrorIcon
import com.example.corpaliv.ui.components.PageIntro
import com.example.corpaliv.viewmodel.InteresCapacitacionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Formulario Compose para registrar interés en una capacitación.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteresCapacitacionScreen(
    modifier: Modifier = Modifier,
    formularioViewModel: InteresCapacitacionViewModel = viewModel(),
    onFormularioValido: () -> Unit
) {
    // Observa los datos y errores que administra el ViewModel.
    val estado by formularioViewModel.estado.collectAsState()
    var mostrarSelectorFecha by remember { mutableStateOf(false) }
    val selectorFecha = rememberDatePickerState(
        initialSelectedDateMillis = estado.fechaPreferidaMillis
    )

    // Formatea en UTC para que el día elegido no cambie por la zona horaria.
    val formatoFecha = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale("es", "CL")).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        PageIntro("Interés en capacitación",
            "Indica cuántas personas participarían y la fecha que prefieres.")
        Text("Tus datos", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = formularioViewModel::onNombreChange,
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
            onValueChange = formularioViewModel::onCorreoChange,
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

        Text("Datos de la actividad", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = estado.cantidadParticipantes,
            onValueChange =
                formularioViewModel::onCantidadParticipantesChange,
            label = { Text("Cantidad de participantes") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = estado.errores.cantidadParticipantes != null,
            trailingIcon = {
                if (estado.errores.cantidadParticipantes != null) ErrorIcon()
            },
            supportingText = {
                estado.errores.cantidadParticipantes?.let { Text(it) }
            }
        )

        // Abre el selector de fecha de Material 3.
        OutlinedTextField(
            value = estado.fechaPreferidaMillis?.let {
                formatoFecha.format(Date(it))
            } ?: "",
            onValueChange = {},
            label = { Text("Fecha preferida") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            isError = estado.errores.fechaPreferida != null,
            supportingText = {
                estado.errores.fechaPreferida?.let { Text(it) }
            },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (estado.errores.fechaPreferida != null) ErrorIcon()
                    TextButton(onClick = { mostrarSelectorFecha = true }) {
                        Text("Elegir")
                    }
                }
            }
        )

        // Checkbox: la autorización es obligatoria para guardar el formulario.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = estado.autorizaContacto,
                onCheckedChange =
                    formularioViewModel::onAutorizaContactoChange
            )
            Text("Acepto guardar estos datos en la aplicación.")
        }

        estado.errores.autorizaContacto?.let { mensaje ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                ErrorIcon()
                Text(mensaje, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }
        }

        // Switch: registra el interés en futuras novedades.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Me interesan futuras novedades")
            Switch(
                checked = estado.recibirNovedades,
                onCheckedChange =
                    formularioViewModel::onRecibirNovedadesChange
            )
        }

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

    if (mostrarSelectorFecha) {
        DatePickerDialog(
            onDismissRequest = {
                mostrarSelectorFecha = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        formularioViewModel.onFechaPreferidaChange(
                            selectorFecha.selectedDateMillis
                        )
                        mostrarSelectorFecha = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarSelectorFecha = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = selectorFecha)
        }
    }
}
