package com.example.proyectofinal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String?,
    val dueTimestamp: Long,      // Milisegundos desde epoch
    val isDone: Boolean = false
)
