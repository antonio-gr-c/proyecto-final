package com.example.proyectofinal.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.repository.TaskRepositoryImpl
import com.example.proyectofinal.domain.model.Task
import com.example.proyectofinal.domain.usecase.GetRandomQuoteUseCase
import com.example.proyectofinal.domain.usecase.GetTasksUseCase
import com.example.proyectofinal.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de UI para la lista de tareas
data class TaskListUiState(
    val isLoading: Boolean = true,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null
)

class TaskListViewModel : ViewModel() {

    // Repositorio y casos de uso
    private val repo = TaskRepositoryImpl()
    private val getTasksUseCase   = GetTasksUseCase(repo)
    private val updateTaskUseCase = UpdateTaskUseCase(repo)
    private val getQuoteUseCase   = GetRandomQuoteUseCase()

    // Estado de UI
    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    // Flujo de eventos “una sola vez” para la frase motivacional
    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow: SharedFlow<String> = _eventFlow.asSharedFlow()

    init {
        // Cargamos las tareas al iniciar
        viewModelScope.launch {
            getTasksUseCase()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { e ->
                    _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
                }
                .collect { list ->
                    _uiState.update { it.copy(tasks = list, isLoading = false) }
                }
        }
    }

    /**
     * Marca o desmarca la tarea. Si queda completada, lanza
     * la petición de frase motivacional y emite el resultado.
     */
    fun toggleDone(task: Task) {
        viewModelScope.launch {
            // 1) Actualizamos el estado en la BD
            val updated = task.copy(isDone = !task.isDone)
            updateTaskUseCase(updated)

            // 2) Si ahora está completada, pedimos la frase
            if (updated.isDone) {
                getQuoteUseCase()
                    .catch { /* aquí podrías emitir un mensaje de error si quisieras */ }
                    .collect { quote ->
                        val text = "\"${quote.text}\" — ${quote.author ?: "Anónimo"}"
                        _eventFlow.emit(text)
                    }
            }
        }
    }
}
