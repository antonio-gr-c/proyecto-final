package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Task
import com.example.proyectofinal.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(
    private val repo: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> =
        repo.getAllTasks()
}
