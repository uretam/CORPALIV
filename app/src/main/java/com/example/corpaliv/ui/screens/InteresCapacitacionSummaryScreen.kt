package com.example.corpaliv.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.corpaliv.ui.components.SavedSummary
import com.example.corpaliv.viewmodel.InteresCapacitacionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun InteresCapacitacionSummaryScreen(
    modifier: Modifier = Modifier,
    formularioViewModel: InteresCapacitacionViewModel,
    onVerSolicitudes: () -> Unit
) {
    val estado by formularioViewModel.estado.collectAsState()
    val formatoFecha = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale("es", "CL")).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }
    val fecha = estado.fechaPreferidaMillis?.let { formatoFecha.format(Date(it)) } ?: "Sin fecha"

    SavedSummary(
        title = "Interés guardado",
        details = listOf(
            "Nombre" to estado.nombre,
            "Correo" to estado.correo,
            "Participantes" to estado.cantidadParticipantes,
            "Fecha preferida" to fecha,
            "Confirmación" to if (estado.autorizaContacto) "Sí" else "No",
            "Interés en novedades" to if (estado.recibirNovedades) "Sí" else "No"
        ),
        modifier = modifier,
        onVerSolicitudes = onVerSolicitudes
    )
}
