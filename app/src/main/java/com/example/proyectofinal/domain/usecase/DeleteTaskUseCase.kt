// DeleteTaskUseCase.kt
package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Task
import com.example.proyectofinal.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val repo: TaskRepository
) {
    suspend operator fun invoke(task: Task) =
        repo.deleteTask(task)
}
