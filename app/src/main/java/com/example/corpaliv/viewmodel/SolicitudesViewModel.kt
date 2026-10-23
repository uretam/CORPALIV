package com.example.corpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpaliv.data.AppDatabase
import com.example.corpaliv.data.SolicitudRepository
import com.example.corpaliv.model.Solicitud
import kotlinx.coroutines.launch

class SolicitudesViewModel(application: Application) : AndroidViewModel(application) {
    private val repositorio = SolicitudRepository(
        AppDatabase.getInstance(application).solicitudDao()
    )

    val solicitudes = repositorio.solicitudes

    fun eliminar(solicitud: Solicitud) {
        viewModelScope.launch { repositorio.eliminar(solicitud) }
    }
}
