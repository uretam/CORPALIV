package com.example.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpaliv.model.Solicitud
import com.example.corpaliv.ui.components.PageIntro
import com.example.corpaliv.viewmodel.SolicitudesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun SolicitudesScreen(
    modifier: Modifier = Modifier,
    solicitudesViewModel: SolicitudesViewModel = viewModel(),
    onCrear: () -> Unit,
    onEditar: (Solicitud) -> Unit
) {
    val solicitudes by solicitudesViewModel.solicitudes.collectAsState(initial = emptyList())
    var pendienteEliminar by remember { mutableStateOf<Solicitud?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PageIntro("Registros",
                "Consulta, edita o elimina los intereses guardados en este dispositivo.")
        }
        if (solicitudes.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Aún no tienes registros", style = MaterialTheme.typography.titleLarge)
                        Text("Registra tu interés para verlo aquí y poder actualizarlo.")
                        Button(onClick = onCrear, modifier = Modifier.fillMaxWidth()) {
                            Text("Ver formas de colaborar")
                        }
                    }
                }
            }
        } else {
            items(solicitudes, key = { it.id }) { solicitud ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(solicitud.tipo, style = MaterialTheme.typography.labelLarge)
                        Text(solicitud.nombre, style = MaterialTheme.typography.titleLarge)
                        Text(solicitud.correo, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (solicitud.tipo == "Colaboración") {
                            Text(solicitud.modalidad, style = MaterialTheme.typography.titleMedium)
                            Text(solicitud.mensaje)
                        } else {
                            Text("Participantes: " + solicitud.cantidad)
                            solicitud.fechaPreferida?.let { fecha ->
                                val formato = SimpleDateFormat("dd/MM/yyyy", Locale("es", "CL"))
                                formato.timeZone = TimeZone.getTimeZone("UTC")
                                Text("Fecha preferida: " + formato.format(Date(fecha)))
                            }
                            Text(if (solicitud.recibirNovedades)
                                "Interés en novedades: sí" else "Interés en novedades: no")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            TextButton(onClick = { onEditar(solicitud) }) { Text("Editar") }
                            TextButton(onClick = { pendienteEliminar = solicitud }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    pendienteEliminar?.let { solicitud ->
        AlertDialog(
            onDismissRequest = { pendienteEliminar = null },
            title = { Text("Eliminar registro") },
            text = { Text("¿Quieres eliminarlo? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    solicitudesViewModel.eliminar(solicitud)
                    pendienteEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { pendienteEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}
