package com.example.corpaliv.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// El resumen aparece con una animación sencilla después de guardar.
@Composable
fun SavedSummary(
    title: String,
    details: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    onVerSolicitudes: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(visible = visible, enter = fadeIn()) {
        Column(
            modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.headlineMedium)
            Text("Tu interés quedó guardado en este dispositivo.",
                style = MaterialTheme.typography.bodyLarge)
            details.forEach { (label, value) ->
                Text("$label: $value", style = MaterialTheme.typography.bodyLarge)
            }
            Button(onClick = onVerSolicitudes, modifier = Modifier.fillMaxWidth()) {
                Text("Ver registros")
            }
        }
    }
}
