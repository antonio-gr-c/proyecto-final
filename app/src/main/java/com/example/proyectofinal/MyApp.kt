package com.example.proyectofinal

import android.app.Application
import com.example.proyectofinal.data.datastore.SettingsDataStore
import com.example.proyectofinal.data.local.DatabaseProvider
import com.google.firebase.FirebaseApp

class MyApp : Application() {

    lateinit var settings: SettingsDataStore
        private set

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // Inicializa Firebase
        DatabaseProvider.init(this)
        settings = SettingsDataStore(this)
    }
}
