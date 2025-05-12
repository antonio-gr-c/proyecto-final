package com.example.proyectofinal

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.proyectofinal.data.datastore.SettingsDataStore
import com.example.proyectofinal.ui.settings.SettingsScreen
import com.example.proyectofinal.ui.taskdetail.TaskDetailScreen
import com.example.proyectofinal.ui.tasklist.TaskListScreen
import com.example.proyectofinal.ui.theme.ProyectoFinalTheme
import com.example.proyectofinal.worker.ReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // En Android 13+ necesitamos pedir permiso para mostrar notifs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        setContent {
            // Programamos la notificación de prueba, pero dentro de Compose
            LaunchedEffect(Unit) {
                val testRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(5, TimeUnit.SECONDS)
                    .build()
                WorkManager.getInstance(this@MainActivity).enqueue(testRequest)
            }

            // Tomamos la instancia de SettingsDataStore
            val settings: SettingsDataStore = (application as MyApp).settings
            val isDarkMode by settings.darkModeFlow.collectAsState(initial = false)

            ProyectoFinalTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "task_list"
                ) {
                    composable("task_list") {
                        TaskListScreen(
                            onTaskClick     = { id -> navController.navigate("task_detail/$id") },
                            onAddClick      = { navController.navigate("task_detail/0") },
                            onSettingsClick = { navController.navigate("settings") }
                        )
                    }
                    composable(
                        route = "task_detail/{taskId}",
                        arguments = listOf(navArgument("taskId") {
                            type = NavType.LongType
                            defaultValue = 0L
                        })
                    ) { backStackEntry ->
                        val taskId = backStackEntry.arguments
                            ?.getLong("taskId")
                            ?.takeIf { it != 0L }

                        TaskDetailScreen(
                            taskId    = taskId,
                            onSaved   = { navController.popBackStack() },
                            onDeleted = { navController.popBackStack() }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(settings = settings)
                    }
                }
            }
        }
    }
}



