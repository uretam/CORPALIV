package com.example.corpaliv.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.corpaliv.ui.components.SavedSummary
import com.example.corpaliv.viewmodel.SolicitudColaboracionViewModel

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    formularioViewModel: SolicitudColaboracionViewModel,
    onVerSolicitudes: () -> Unit
) {
    val estado by formularioViewModel.estado.collectAsState()
    SavedSummary(
        title = "Colaboración guardada",
        details = listOf(
            "Nombre" to estado.nombre,
            "Correo" to estado.correo,
            "Forma de colaborar" to estado.tipoColaboracion,
            "Mensaje" to estado.mensaje
        ),
        modifier = modifier,
        onVerSolicitudes = onVerSolicitudes
    )
}
