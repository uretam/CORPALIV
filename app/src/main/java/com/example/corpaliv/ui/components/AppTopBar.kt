package com.example.corpaliv.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

// Botón que abre el menú y nombre de la aplicación.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = { Text("CORPALIV") },
        navigationIcon = {
            TextButton(onClick = onMenuClick) { Text("Menú") }
        }
    )
}
