package com.example.proyectofinal.ui.taskdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Long?,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: TaskDetailViewModel = viewModel()
) {
    // Observa el estado
    val uiState by viewModel.uiState.collectAsState()

    // Carga la tarea al iniciarse
    LaunchedEffect(taskId) {
        viewModel.load(taskId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isNew) "Nueva tarea" else "Editar tarea")
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(padding)
            ) {
                TextField(
                    value = uiState.task.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = uiState.task.description.orEmpty(),
                    onValueChange = viewModel::onDescriptionChange,
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))



                Button(
                    onClick = { viewModel.save(onDone = onSaved) },
                    enabled = !uiState.isSaving,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(if (uiState.isNew) "Crear" else "Guardar")
                }

                if (!uiState.isNew) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.delete(onDone = onDeleted) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    )
}
