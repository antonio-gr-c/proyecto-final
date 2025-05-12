// app/src/main/java/com/example/proyectofinal/worker/ReminderWorker.kt
package com.example.proyectofinal.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    private val appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val CHANNEL_ID = "task_reminders"
        const val INPUT_TASK_ID = "taskId"
        const val INPUT_TASK_TITLE = "taskTitle"
    }

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        // 1) Extraemos los parámetros
        val taskId    = inputData.getLong(INPUT_TASK_ID, 0L)
        val taskTitle = inputData.getString(INPUT_TASK_TITLE) ?: "Tarea pendiente"

        // 2) Creamos el canal sólo en API ≥ 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        // 3) Construimos la notificación
        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            // Usamos un icono del sistema para evitar errores; reemplázalo luego por tu propio drawable
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Recordatorio de tarea")
            .setContentText("Tienes pendiente: $taskTitle")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // 4) La mostramos (requiere permiso POST_NOTIFICATIONS en Android 13+)
        NotificationManagerCompat
            .from(appContext)
            .notify(taskId.toInt(), notification)

        return Result.success()
    }

    @SuppressLint("NewApi")
    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Recordatorios de tareas",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones cuando venza una tarea"
        }

        val manager = appContext
            .getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
