// UpdateTaskUseCase.kt
package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Task
import com.example.proyectofinal.domain.repository.TaskRepository

class UpdateTaskUseCase(
    private val repo: TaskRepository
) {
    suspend operator fun invoke(task: Task) =
        repo.updateTask(task)
}
