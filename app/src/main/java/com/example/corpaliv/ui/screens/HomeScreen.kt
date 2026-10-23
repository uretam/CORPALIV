package com.example.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.corpaliv.ui.theme.CORPALIVTheme

// Explica el propósito y lleva a la acción principal.
@Composable
fun HomeScreen(modifier: Modifier = Modifier, onVerFormas: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("CORPALIV y Alba Lab", style = MaterialTheme.typography.headlineMedium)
        Text(
            "CORPALIV promueve la autonomía de personas con discapacidad múltiple. " +
                "La Escuela Especial Jan Van Dijk y Alba Lab trabajan en educación y formación para el trabajo.",
            style = MaterialTheme.typography.bodyLarge
        )
        Text("Tu empresa puede apoyar educación, formación e inclusión laboral.",
            style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onVerFormas, modifier = Modifier.fillMaxWidth()) {
            Text("Ver formas de colaborar")
        }
        Text("El interés se registra en este dispositivo y se puede revisar en Registros.",
            style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    CORPALIVTheme { HomeScreen(onVerFormas = {}) }
}
