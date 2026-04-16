package com.jbonet.lxquest

import android.app.Application
import androidx.room.Room
import com.jbonet.lxquest.data.entities.LxQuestDatabase
import com.jbonet.lxquest.data.repository.LxQuestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Clase principal de la aplicación, instancia de la base de datos y el respositorio. */
class LxQuestApplication : Application() {
    val database by lazy {
        Room.databaseBuilder(
            this,
            LxQuestDatabase::class.java,
            "lxquest_database").build()
    }
    val repository by lazy {
        LxQuestRepository(database.lxQuestDao())
    }
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            repository.poblarCatalogoInicial()
        }
    }
}