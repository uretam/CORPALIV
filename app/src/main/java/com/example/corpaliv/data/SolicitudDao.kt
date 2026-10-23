package com.example.corpaliv.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.corpaliv.model.Solicitud
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {
    @Query("SELECT * FROM solicitudes ORDER BY fechaCreacion DESC")
    fun observarTodas(): Flow<List<Solicitud>>

    @Insert
    suspend fun insertar(solicitud: Solicitud)

    @Update
    suspend fun actualizar(solicitud: Solicitud)

    @Delete
    suspend fun eliminar(solicitud: Solicitud)
}
