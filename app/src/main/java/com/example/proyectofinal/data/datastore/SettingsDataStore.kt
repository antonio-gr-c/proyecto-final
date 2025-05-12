package com.example.proyectofinal.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Define el nombre del DataStore
private const val SETTINGS_NAME = "user_settings"
// 2. Delegate para crear el DataStore<Preferences>
private val Context.dataStore by preferencesDataStore(SETTINGS_NAME)

object PreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
}

class SettingsDataStore(private val context: Context) {

    // Flujo que emite el valor de modo oscuro (default = false)
    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[PreferencesKeys.DARK_MODE] ?: false
        }

    // Flujo que emite si notificaciones están activas (default = true)
    val notificationsFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[PreferencesKeys.NOTIFICATIONS] ?: true
        }

    // Función para actualizar modo oscuro
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.DARK_MODE] = enabled
        }
    }

    // Función para actualizar notificaciones
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.NOTIFICATIONS] = enabled
        }
    }
}
