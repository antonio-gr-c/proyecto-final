package com.example.proyectofinal.domain.repository

import com.example.proyectofinal.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    suspend fun getTaskById(id: Long): Task?

    suspend fun createTask(task: Task): Long

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}
