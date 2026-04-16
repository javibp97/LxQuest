package com.jbonet.lxquest.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Entidad que introduce las notas del usuario sobre una distribución en concreto. */
// @Suppress("PropertyName") --> Se ignora camelCase para mantener consistencia con snake_case.
@Suppress("PropertyName")
@Entity
data class Nota(@PrimaryKey(autoGenerate = true)
    val id_nota: Int = 0,
    val id_distro: Int,
    val id_usuario: Int,
    val titulo: String = "Nota rápida",
    val contenido: String,
    val fecha_guardado: String = "Sin fecha")