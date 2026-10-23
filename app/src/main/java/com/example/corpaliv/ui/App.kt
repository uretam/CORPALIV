package com.example.corpaliv.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpaliv.data.NotificacionLocal
import com.example.corpaliv.ui.components.AppBottomBar
import com.example.corpaliv.ui.components.AppDrawer
import com.example.corpaliv.ui.components.AppTopBar
import com.example.corpaliv.ui.screens.CollaborateScreen
import com.example.corpaliv.ui.screens.FormasScreen
import com.example.corpaliv.ui.screens.HomeScreen
import com.example.corpaliv.ui.screens.InteresCapacitacionScreen
import com.example.corpaliv.ui.screens.InteresCapacitacionSummaryScreen
import com.example.corpaliv.ui.screens.SolicitudesScreen
import com.example.corpaliv.ui.screens.SummaryScreen
import com.example.corpaliv.viewmodel.InteresCapacitacionViewModel
import com.example.corpaliv.viewmodel.SolicitudColaboracionViewModel

// La variable pantallaActual decide qué pantalla se dibuja.
@Composable
fun App() {
    var pantallaActual by remember { mutableStateOf("Inicio") }
    val colaboracionViewModel: SolicitudColaboracionViewModel = viewModel()
    val capacitacionViewModel: InteresCapacitacionViewModel = viewModel()
    val context = LocalContext.current

    val permisoNotificacion = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) NotificacionLocal.mostrar(context)
    }

    val avisarGuardado: () -> Unit = {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permisoNotificacion.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            NotificacionLocal.mostrar(context)
        }
    }

    AppDrawer(
        pantallaActual = pantallaActual,
        onPantallaSeleccionada = { pantalla ->
            if (pantalla == "Capacitación") capacitacionViewModel.nuevoFormulario()
            pantallaActual = pantalla
        }
    ) { abrirMenu ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppTopBar(onMenuClick = abrirMenu) },
            bottomBar = {
                AppBottomBar(pantallaActual = pantallaActual) { pantallaActual = it }
            }
        ) { espacio ->
            val modifier = Modifier.padding(espacio)
            when (pantallaActual) {
                "Inicio" -> HomeScreen(
                    modifier = modifier,
                    onVerFormas = { pantallaActual = "Formas" }
                )

                "Formas" -> FormasScreen(
                    modifier = modifier,
                    onElegirForma = { forma ->
                        colaboracionViewModel.nuevoFormulario()
                        colaboracionViewModel.onTipoColaboracionChange(forma)
                        pantallaActual = "Contacto"
                    },
                    onCapacitacion = {
                        capacitacionViewModel.nuevoFormulario()
                        pantallaActual = "Capacitación"
                    }
                )

                "Contacto" -> CollaborateScreen(
                    modifier = modifier,
                    formularioViewModel = colaboracionViewModel,
                    onFormularioValido = {
                        pantallaActual = "Resumen"
                        avisarGuardado()
                    }
                )

                "Capacitación" -> InteresCapacitacionScreen(
                    modifier = modifier,
                    formularioViewModel = capacitacionViewModel,
                    onFormularioValido = {
                        pantallaActual = "Resumen capacitación"
                        avisarGuardado()
                    }
                )

                "Resumen" -> SummaryScreen(
                    modifier = modifier,
                    formularioViewModel = colaboracionViewModel,
                    onVerSolicitudes = { pantallaActual = "Registros" }
                )

                "Resumen capacitación" -> InteresCapacitacionSummaryScreen(
                    modifier = modifier,
                    formularioViewModel = capacitacionViewModel,
                    onVerSolicitudes = { pantallaActual = "Registros" }
                )

                "Registros" -> SolicitudesScreen(
                    modifier = modifier,
                    onCrear = { pantallaActual = "Formas" },
                    onEditar = { solicitud ->
                        if (solicitud.tipo == "Capacitación") {
                            capacitacionViewModel.cargarSolicitud(solicitud)
                            pantallaActual = "Capacitación"
                        } else {
                            colaboracionViewModel.cargarSolicitud(solicitud)
                            pantallaActual = "Contacto"
                        }
                    }
                )
            }
        }
    }
}
