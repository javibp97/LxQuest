package com.jbonet.lxquest.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

// @Suppress("PropertyName") --> Se ignora camelCase para mantener consistencia con snake_case.
/** La siguiente entidad gestiona las distribuciones marcadas como favoritas. */
@Suppress("PropertyName")
@Entity( tableName = "Favorito",
    foreignKeys = [ForeignKey(
            entity = Distribucion::class,
            parentColumns = ["id_distro"],
            childColumns = ["id_distro"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.NO_ACTION)])
data class Favorito(
    @PrimaryKey(autoGenerate = true)
    val id_favorito: Int = 0,
    val id_distro: Int,
    val id_usuario: Int,
    val fecha_guardado: String) /**  Formato de fecha: dd/MM/yyyy. */