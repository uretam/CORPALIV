package com.example.corpaliv.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.corpaliv.R

// Tres accesos para el recorrido principal.
@Composable
fun AppBottomBar(pantallaActual: String, onSeleccionar: (String) -> Unit) {
    val secciones = listOf(
        Triple("Inicio", "Inicio", R.drawable.ic_home),
        Triple("Formas", "Colaborar", R.drawable.ic_heart),
        Triple("Registros", "Registros", R.drawable.ic_list)
    )
    NavigationBar {
        secciones.forEach { (pantalla, nombre, icono) ->
            val seleccionada = pantallaActual == pantalla ||
                (pantalla == "Formas" && pantallaActual in listOf(
                    "Contacto", "Capacitación", "Resumen", "Resumen capacitación"
                ))
            NavigationBarItem(
                selected = seleccionada,
                onClick = { onSeleccionar(pantalla) },
                icon = { Icon(painterResource(icono), contentDescription = null) },
                label = { Text(nombre) }
            )
        }
    }
}
