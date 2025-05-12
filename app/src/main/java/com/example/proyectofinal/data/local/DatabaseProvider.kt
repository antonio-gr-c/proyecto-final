package com.example.proyectofinal.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private const val DB_NAME = "tasks_db"

    // Lateinit para inicializar en Application
    lateinit var db: AppDatabase
        private set

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }

    // Acceso directo al DAO
    val taskDao: TaskDao
        get() = db.taskDao()
}
