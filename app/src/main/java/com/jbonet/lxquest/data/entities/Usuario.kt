package com.jbonet.lxquest.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Entidad que almacena el perfil de usuario, son usadas como filtro para las consultas. */
// @Suppress("PropertyName") --> Se ignora camelCase para mantener consistencia con snake_case.
@Suppress("PropertyName")
@Entity(tableName = "Usuario")
data class Usuario(@PrimaryKey(autoGenerate = true)
    val id_usuario: Int = 0,
    val nombre_usuario: String,
    val ram_equipo: Int,
    val cpu_equipo: String,
    val disco_duro: Int,
    val uso_habitual: String)