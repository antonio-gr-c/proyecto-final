package com.example.proyectofinal.data.repository

import com.example.proyectofinal.data.local.DatabaseProvider
import com.example.proyectofinal.data.local.toDomain
import com.example.proyectofinal.data.local.toEntity
import com.example.proyectofinal.domain.model.Task
import com.example.proyectofinal.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl : TaskRepository {

    private val dao = DatabaseProvider.taskDao

    override fun getAllTasks(): Flow<List<Task>> =
        dao.getAllTasks()
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getTaskById(id: Long): Task? =
        dao.getTaskById(id)?.toDomain()

    override suspend fun createTask(task: Task): Long =
        dao.insertTask(task.toEntity())

    override suspend fun updateTask(task: Task) {
        dao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task.toEntity())
    }
}
