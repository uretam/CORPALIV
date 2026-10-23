package com.example.corpaliv.data

import com.example.corpaliv.model.Solicitud

// Sigue la misma secuencia del ejemplo de notas: pantalla → ViewModel → repositorio → DAO.
class SolicitudRepository(private val dao: SolicitudDao) {
    val solicitudes = dao.observarTodas()

    suspend fun guardar(solicitud: Solicitud) {
        dao.insertar(solicitud)
    }

    suspend fun actualizar(solicitud: Solicitud) {
        dao.actualizar(solicitud)
    }

    suspend fun eliminar(solicitud: Solicitud) {
        dao.eliminar(solicitud)
    }
}
