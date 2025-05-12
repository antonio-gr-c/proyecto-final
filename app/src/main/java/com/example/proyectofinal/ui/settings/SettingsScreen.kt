package com.example.proyectofinal.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.MyApp
import com.example.proyectofinal.data.datastore.SettingsDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: SettingsDataStore = (LocalContext.current.applicationContext as MyApp).settings,
    navController: NavController? = null // 👈 agregado para logout
) {
    val scope = rememberCoroutineScope()
    val darkMode by settings.darkModeFlow.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ajustes") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Modo oscuro", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = darkMode,
                    onCheckedChange = {
                        scope.launch { settings.setDarkMode(it) }
                    }
                )
            }

            // 👇 Botón para cerrar sesión
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController?.navigate("login") {
                        popUpTo("task_list") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
