package com.jbonet.lxquest.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
// @Suppress("PropertyName") --> Se ignora camelCase para mantener consistencia con snake_case.
/** La siguiente entidad contiene la tabla de distribuciones. */
@Suppress("PropertyName")
@Entity
data class Distribucion(@PrimaryKey(autoGenerate = true) val id_distro: Int = 0,
    val nombre: String,
    val base_sistema: String,
    val entorno_grafico: String,
    val ciclo_actualizacion: String,
    val ram_minima: Int,
    val cpu_minima: String,
    val espacio_disco: Int,
    val uso_recomendado: String,
    val gestor_paquetes: String)