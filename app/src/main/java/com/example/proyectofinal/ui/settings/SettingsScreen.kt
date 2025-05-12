package com.example.proyectofinal.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.MyApp
import com.example.proyectofinal.data.datastore.SettingsDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    // Obtenemos el DataStore directamente desde la Application
    settings: SettingsDataStore = (LocalContext.current.applicationContext as MyApp).settings
) {
    val scope = rememberCoroutineScope()
    // Leemos el Flow de modo oscuro como State
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
                .padding(16.dp)
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
        }
    }
}
