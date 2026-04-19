package com.jbonet.lxquest.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Entidad que introduce las notas del usuario sobre una distribución en concreto. */
// @Suppress("PropertyName") --> Se ignora camelCase para mantener consistencia con snake_case.
@Suppress("PropertyName")
@Entity(
tableName = "Nota",
foreignKeys = [ForeignKey(
        entity = Distribucion::class,
        parentColumns = ["id_distro"],
        childColumns = ["id_distro"],
        onDelete = ForeignKey.CASCADE),
ForeignKey(
entity = Usuario::class,
parentColumns = ["id_usuario"],
childColumns = ["id_usuario"],
onDelete = ForeignKey.CASCADE)],
indices = [
    Index(value = ["id_distro"]),
    Index(value = ["id_usuario"])]
)
data class Nota(@PrimaryKey(autoGenerate = true)
    val id_nota: Int = 0,
    val id_distro: Int,
    val id_usuario: Int,
    val titulo: String = "Nota rápida",
    val contenido: String,
    val fecha_guardado: String = "Sin fecha")