package com.example.proyectofinal

import android.app.Application
import com.example.proyectofinal.data.datastore.SettingsDataStore  // <— Import necesario
import com.example.proyectofinal.data.local.DatabaseProvider

class MyApp : Application() {
    // late-init para el DataStore
    lateinit var settings: SettingsDataStore
        private set

    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.init(this)
        settings = SettingsDataStore(this)
    }
}
