package com.example.corpaliv.ui.components

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

// Abre la cámara y muestra la foto recibida dentro de esta pantalla.
@Composable
fun CameraCard() {
    var foto by remember { mutableStateOf<Bitmap?>(null) }
    var error by remember { mutableStateOf(false) }
    val abrirCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { imagen -> foto = imagen }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Vista previa con la cámara", style = MaterialTheme.typography.titleMedium)
            Text("La foto aparece aquí, pero no se adjunta al registro.")
            OutlinedButton(onClick = {
                try {
                    error = false
                    abrirCamara.launch(null)
                } catch (e: ActivityNotFoundException) {
                    error = true
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Abrir cámara")
            }
            if (error) {
                Text("No se encontró una aplicación de cámara.",
                    color = MaterialTheme.colorScheme.error)
            }
            foto?.let { imagen ->
                Image(
                    bitmap = imagen.asImageBitmap(),
                    contentDescription = "Foto tomada con la cámara",
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
