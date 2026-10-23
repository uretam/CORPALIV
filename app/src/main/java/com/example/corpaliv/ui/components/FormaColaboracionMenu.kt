package com.example.corpaliv.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Menú desplegable adaptado del componente de clase.
@Composable
fun FormaColaboracionMenu(
    valorSeleccionado: String,
    errorMensaje: String?,
    onSeleccionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpandido by remember {
        mutableStateOf(false)
    }

    // Opciones basadas en las formas de colaboración del caso CORPALIV.
    val opciones = listOf(
        "Donación",
        "Alianza con empresas",
        "Voluntariado",
        "Productos y servicios"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Forma de colaborar",
            style = MaterialTheme.typography.bodyMedium
        )

        Box {
            OutlinedButton(
                onClick = {
                    menuExpandido = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = valorSeleccionado.ifBlank {
                        "Selecciona una opción"
                    }
                )
            }

            DropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = {
                    menuExpandido = false
                }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = {
                            Text(opcion)
                        },
                        onClick = {
                            onSeleccionar(opcion)
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        errorMensaje?.let { mensaje ->
            Row(modifier = Modifier.padding(start = 16.dp)) {
                ErrorIcon()
                Text(mensaje, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
