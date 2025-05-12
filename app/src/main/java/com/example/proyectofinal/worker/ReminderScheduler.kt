package com.example.proyectofinal.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    private const val UNIQUE_WORK_NAME = "task_reminder_work"

    /**
     * Programa un recordatorio periódico.
     *
     * @param context contexto de la app
     * @param intervalMinutes cada cuántos minutos se disparará la notificación
     * @param taskId el ID de la tarea (se usa como identificador de notificación)
     * @param taskTitle título de la tarea (para mostrar en el notification text)
     */
    fun schedule(
        context: Context,
        intervalMinutes: Long,
        taskId: Long,
        taskTitle: String
    ) {
        // Preparamos los datos de entrada
        val data = Data.Builder()
            .putLong(ReminderWorker.INPUT_TASK_ID, taskId)
            .putString(ReminderWorker.INPUT_TASK_TITLE, taskTitle)
            .build()

        // Creamos un PeriodicWorkRequest
        val work = PeriodicWorkRequestBuilder<ReminderWorker>(
            intervalMinutes, TimeUnit.MINUTES
        )
            .setInputData(data)
            // Si ya existía otro igual, lo reemplazamos
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                work
            )
    }

    /**
     * Cancela el recordatorio periódico.
     */
    fun cancel(context: Context) {
        WorkManager
            .getInstance(context)
            .cancelUniqueWork(UNIQUE_WORK_NAME)
    }
}
