package com.jbonet.lxquest.data.entities

import androidx.room.Database
import androidx.room.RoomDatabase

/** Acceso principal a la persistencia de datos de la app. */
@Database(entities =
    [Distribucion::class,
        Usuario::class,
        Favorito::class,
        Nota::class], version = 1,
    exportSchema = false)
abstract class LxQuestDatabase : RoomDatabase() {
    abstract fun lxQuestDao(): LxQuestDao
}