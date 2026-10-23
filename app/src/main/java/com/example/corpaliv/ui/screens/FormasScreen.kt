package com.example.corpaliv.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.corpaliv.ui.components.CameraCard
import com.example.corpaliv.ui.components.PageIntro

@Composable
fun FormasScreen(
    modifier: Modifier = Modifier,
    onElegirForma: (String) -> Unit,
    onCapacitacion: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PageIntro("Formas de colaborar", "Elige una opción y registra tu interés.")

        FormaCard("Alianza con empresas", "Conversemos sobre una alianza.",
            "Puede apoyar la inclusión laboral.") {
            onElegirForma("Alianza con empresas")
        }
        FormaCard("Donación", "Consulta cómo aportar.",
            "Puede apoyar educación y formación.") {
            onElegirForma("Donación")
        }
        FormaCard("Voluntariado", "Ofrece tiempo o habilidades de tu equipo.",
            "Puede apoyar actividades de formación.") {
            onElegirForma("Voluntariado")
        }
        FormaCard("Productos y servicios", "Consulta productos de talleres, servicios y charlas.",
            "Puede apoyar experiencias de aprendizaje.") {
            onElegirForma("Productos y servicios")
        }
        FormaCard("Capacitación", "Consulta una actividad para tu equipo.",
            "Puede apoyar la sensibilización e inclusión.", onCapacitacion)

        Text("Otras funciones", style = MaterialTheme.typography.titleMedium)
        OutlinedButton(
            onClick = {
                val compartir = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT,
                        "Conoce CORPALIV y Alba Lab: formación, inclusión y colaboración con empresas.")
                }
                try {
                    context.startActivity(Intent.createChooser(compartir, "Compartir propósito"))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "No hay una aplicación para compartir.",
                        Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Compartir propósito") }
        CameraCard()
    }
}

@Composable
private fun FormaCard(titulo: String, descripcion: String, destino: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(descripcion, style = MaterialTheme.typography.bodyLarge)
            Text(destino, style = MaterialTheme.typography.bodyLarge)
            OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
                Text("Elegir $titulo")
            }
        }
    }
}
