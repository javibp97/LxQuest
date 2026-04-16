package com.jbonet.lxquest.data.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Definición de las operaciones CRUD y consultas personalizadas para el filtrado de Hardware.  */
@Dao
interface LxQuestDao {
    @Query("SELECT * FROM Distribucion")
    fun obtenerDistribuciones(): Flow<List<Distribucion>>
    /** Consulta principal */
    @Query("SELECT * FROM Distribucion WHERE id_distro = :id LIMIT 1")
    fun obtenerDetallesDistro(id: Int): Flow<Distribucion>
    /** Filtro según requisitos de hardware.  */
    @Query("""
        SELECT * FROM Distribucion 
        WHERE ram_minima <= :ram 
        AND espacio_disco <= :disco 
        AND uso_recomendado = :uso
        AND (cpu_minima = :cpu OR :cpu = '64 bits')
    """)
    fun filtrarDistribuciones(ram: Int, disco: Int, uso: String, cpu: String): Flow<List<Distribucion>>
    @Query("SELECT COUNT(*) FROM Distribucion")
    suspend fun contarDistribuciones(): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistribucion(distribucion: Distribucion)
    // Gestión del perfil de usuario.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsuario(usuario: Usuario): Long
    @Update
    suspend fun actualizarUsuario(usuario: Usuario)
    @Query("SELECT * FROM Usuario LIMIT 1")
    fun obtenerPerfilUsuario(): Flow<Usuario?>
    // Lógica de favoritos.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorito(favorito: Favorito)
    @Query("SELECT * FROM Favorito WHERE id_distro = :idDistro AND id_usuario = :idUsuario LIMIT 1")
    fun esFavorito(idDistro: Int, idUsuario: Int): Flow<Favorito?>
    @Query("DELETE FROM Favorito WHERE id_distro = :idDistro AND id_usuario = :idUsuario")
    suspend fun eliminarFavorito(idDistro: Int, idUsuario: Int)
    // Gestión de las notas.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNota(nota: Nota)
    @Query("SELECT * FROM Nota WHERE id_distro = :idDistro")
    fun obtenerNotasPorDistro(idDistro: Int): Flow<List<Nota>>
    @Delete
    suspend fun eliminarNota(nota: Nota)
    @Query("SELECT Distribucion.* FROM Distribucion INNER JOIN Favorito ON Distribucion.id_distro = Favorito.id_distro WHERE Favorito.id_usuario = :idUsuario")
    fun obtenerDistrosFavoritas(idUsuario: Int): Flow<List<Distribucion>>
}