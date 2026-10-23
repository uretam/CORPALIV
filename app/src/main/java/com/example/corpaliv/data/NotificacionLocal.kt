package com.example.corpaliv.data

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.corpaliv.MainActivity
import com.example.corpaliv.R

// Muestra un aviso del dispositivo después de guardar una solicitud en Room.
object NotificacionLocal {
    private const val canalId = "solicitudes_guardadas"

    fun mostrar(context: Context) {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val administrador = context.getSystemService(NotificationManager::class.java)
        administrador.createNotificationChannel(
            NotificationChannel(canalId, "Solicitudes guardadas", NotificationManager.IMPORTANCE_DEFAULT)
        )

        val abrirApp = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = Notification.Builder(context, canalId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Solicitud guardada")
            .setContentText("Puedes consultar tu solicitud en Registros.")
            .setContentIntent(abrirApp)
            .setAutoCancel(true)
            .build()

        administrador.notify(1, notificacion)
    }
}
