package com.jbonet.lxquest.data.repository

import com.jbonet.lxquest.data.entities.Distribucion
import com.jbonet.lxquest.data.entities.Favorito
import com.jbonet.lxquest.data.entities.LxQuestDao
import com.jbonet.lxquest.data.entities.Usuario
import com.jbonet.lxquest.data.entities.Nota
import kotlinx.coroutines.flow.Flow

class LxQuestRepository(private val dao: LxQuestDao) {
    // Lógica del filtro DetallesDistro.
    fun obtenerDetallesDistro(id: Int): Flow<Distribucion> = dao.obtenerDetallesDistro(id)
    fun filtrarHardware(ram: Int, disco: Int, uso: String, cpu: String): Flow<List<Distribucion>> {
        return dao.filtrarDistribuciones(ram, disco, uso, cpu)
    }
    /** Indico la ficha técnica de las distribuciones en la base de datos. */
    suspend fun poblarCatalogoInicial() {
        if (dao.contarDistribuciones() == 0) {
            val distros = listOf(
                Distribucion(nombre = "Ubuntu", base_sistema = "Debian", entorno_grafico = "GNOME", ciclo_actualizacion = "LTS", ram_minima = 4, cpu_minima = "64 bits", espacio_disco = 25, uso_recomendado = "Desarrollo", gestor_paquetes = "apt"),
                Distribucion(nombre = "Linux Mint", base_sistema = "Ubuntu", entorno_grafico = "Cinnamon", ciclo_actualizacion = "LTS", ram_minima = 2, cpu_minima = "64 bits", espacio_disco = 20, uso_recomendado = "Ofimática", gestor_paquetes = "apt"),
                Distribucion(nombre = "Pop!_OS", base_sistema = "Ubuntu", entorno_grafico = "COSMIC", ciclo_actualizacion = "LTS", ram_minima = 4, cpu_minima = "64 bits", espacio_disco = 20, uso_recomendado = "Gaming", gestor_paquetes = "apt"),
                Distribucion(nombre = "Bazzite", base_sistema = "Fedora", entorno_grafico = "KDE Plasma", ciclo_actualizacion = "Rolling Release", ram_minima = 8, cpu_minima = "64 bits", espacio_disco = 40, uso_recomendado = "Gaming", gestor_paquetes = "dnf"),
                Distribucion(nombre = "Puppy Linux", base_sistema = "Base Independiente", entorno_grafico = "JWM", ciclo_actualizacion = "Point-Release", ram_minima = 1, cpu_minima = "32 bits", espacio_disco = 2, uso_recomendado = "Ofimática", gestor_paquetes = "PPM"))
            distros.forEach {dao.insertDistribucion(it)}
        }
    }
    // Función para obtener el perfil de usuario.
    fun obtenerPerfil(): Flow<Usuario?> = dao.obtenerPerfilUsuario()
    suspend fun guardarPerfil(usuario: Usuario) {
        val usuarioFijo = usuario.copy(id_usuario = 1)

        val id = dao.insertUsuario(usuarioFijo)
        if (id == -1L) {
            dao.actualizarUsuario(usuarioFijo)
        }
        dao.insertUsuario(usuarioFijo)
    }
    // Función Favorito.
    fun esFavorito(idDistro: Int, idUsuario: Int): Flow<Favorito?> = dao.esFavorito(idDistro, idUsuario)
    suspend fun gestionarFavorito(favorito: Favorito, existe: Boolean) {
        if (existe) {
            dao.eliminarFavorito(favorito.id_distro, favorito.id_usuario)
        } else {
            dao.insertFavorito(favorito)
        }
    }
    suspend fun eliminarFavorito(idDistro: Int, idUsuario: Int) {
        dao.eliminarFavorito(idDistro, idUsuario)
    }
    // Función Notas.
    fun obtenerNotas(idDistro: Int):Flow<List<Nota>> = dao.obtenerNotasPorDistro(idDistro)
    fun obtenerDistrosFavoritas(idUsuario: Int): Flow<List<Distribucion>> {
        return dao.obtenerDistrosFavoritas(idUsuario)
    }
    suspend fun guardarNota(nota: Nota) = dao.insertNota(nota)
    suspend fun eliminarNota(nota: Nota) = dao.eliminarNota(nota)
}