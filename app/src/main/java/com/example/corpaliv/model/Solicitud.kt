package com.example.corpaliv.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Una fila representa una intención guardada solo en este dispositivo.
@Entity(tableName = "solicitudes")
data class Solicitud(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,
    val nombre: String,
    val correo: String,
    val modalidad: String = "",
    val mensaje: String = "",
    val cantidad: Int? = null,
    val fechaPreferida: Long? = null,
    val recibirNovedades: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis()
)
