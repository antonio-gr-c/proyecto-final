@file:Suppress("SpellCheckingInspection")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.proyectofinal.ui.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.proyectofinal.R
import com.example.proyectofinal.domain.model.Task
import java.text.DateFormat
import java.util.*

@Composable
fun TaskListScreen(
    onTaskClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit,
    taskListViewModel: TaskListViewModel = viewModel()
) {
    // 1) Estado de UI
    val uiState by taskListViewModel.uiState.collectAsState()

    // 2) SnackbarHost para mostrar frases motivacionales
    val snackbarHostState = remember { SnackbarHostState() }

    // 3) Lottie: cargamos la composición desde raw
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.motivation)
    )
    // controlamos si está sonando la animación
    var playAnimation by remember { mutableStateOf(false) }
    // obtenemos el progreso a partir de la composición
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = playAnimation,
        iterations = 1
    )

    // 4) Recoger evento de frase y disparar Snackbar + animación
    LaunchedEffect(taskListViewModel.eventFlow) {
        taskListViewModel.eventFlow.collect { message ->

            // arrancamos la animación
            playAnimation = true
            // mostramos la frase
            snackbarHostState.showSnackbar(message)

        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${uiState.errorMessage}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.tasks) { task ->
                            TaskItem(
                                task = task,
                                onToggle = { taskListViewModel.toggleDone(task) },
                                onClick = { onTaskClick(task.id) }
                            )
                            Divider() // o HorizontalDivider()
                        }
                    }
                }
            }

            // 5) Overlay de Lottie en el centro, si playAnimation == true
            if (playAnimation) {
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .size(180.dp)
                        .align(Alignment.Center)
                )
                // opcional: cuando termine la animación, la ocultas
                if (progress == 1f) {
                    playAnimation = false
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Checkbox(
            checked = task.isDone,
            onCheckedChange = { onToggle() }
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else null
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = DateFormat.getDateTimeInstance().format(Date(task.dueTimestamp)),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
