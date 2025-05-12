package com.example.proyectofinal.ui.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.repository.TaskRepositoryImpl
import com.example.proyectofinal.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TaskDetailUiState(
    val task: com.example.proyectofinal.domain.model.Task =
        com.example.proyectofinal.domain.model.Task(
            title = "",
            dueTimestamp = System.currentTimeMillis()
        ),
    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null
)

class TaskDetailViewModel : ViewModel() {
    private val repo = TaskRepositoryImpl()
    // instanciamos aquí los casos de uso
    private val getTasks    = GetTasksUseCase(repo)
    private val createTask  = CreateTaskUseCase(repo)
    private val updateTask  = UpdateTaskUseCase(repo)
    private val deleteTask  = DeleteTaskUseCase(repo)

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState

    /** Carga la tarea si viene un ID válido (>0) */
    fun load(id: Long?) = viewModelScope.launch {
        if (id != null && id > 0) {
            getTasks()
                .collect { list ->
                    list.find { it.id == id }?.let { found ->
                        _uiState.value = _uiState.value.copy(
                            task = found,
                            isNew = false
                        )
                    }
                }
        }
    }

    fun onTitleChange(new: String) {
        _uiState.value = _uiState.value.copy(
            task = _uiState.value.task.copy(title = new)
        )
    }

    fun onDescriptionChange(new: String) {
        _uiState.value = _uiState.value.copy(
            task = _uiState.value.task.copy(description = new)
        )
    }

    fun onDueChange(timestamp: Long) {
        _uiState.value = _uiState.value.copy(
            task = _uiState.value.task.copy(dueTimestamp = timestamp)
        )
    }

    fun save(onDone: () -> Unit) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isSaving = true)
        try {
            if (_uiState.value.isNew) {
                createTask(_uiState.value.task)
            } else {
                updateTask(_uiState.value.task)
            }
            onDone()
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = e.message)
        } finally {
            _uiState.value = _uiState.value.copy(isSaving = false)
        }
    }

    fun delete(onDone: () -> Unit) = viewModelScope.launch {
        deleteTask(_uiState.value.task)
        onDone()
    }
}
