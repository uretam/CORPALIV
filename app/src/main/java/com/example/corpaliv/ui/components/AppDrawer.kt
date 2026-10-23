package com.example.corpaliv.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Menú deslizante basado en el ejercicio de clase.
@Composable
fun AppDrawer(
    pantallaActual: String,
    onPantallaSeleccionada: (String) -> Unit,
    contenido: @Composable (abrirMenu: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val opciones = listOf(
        "Inicio" to "Inicio",
        "Formas" to "Colaborar",
        "Capacitación" to "Capacitación",
        "Registros" to "Registros"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("CORPALIV · Alba Lab", modifier = Modifier.padding(16.dp))
                opciones.forEach { (pantalla, nombre) ->
                    NavigationDrawerItem(
                        label = { Text(nombre) },
                        selected = pantallaActual == pantalla,
                        onClick = {
                            onPantallaSeleccionada(pantalla)
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        contenido { scope.launch { drawerState.open() } }
    }
}
