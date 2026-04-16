package com.jbonet.lxquest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.firstOrNull
import com.jbonet.lxquest.data.entities.Distribucion
import com.jbonet.lxquest.data.entities.Nota
import com.jbonet.lxquest.data.entities.Usuario
import com.jbonet.lxquest.data.entities.Favorito
import com.jbonet.lxquest.data.repository.LxQuestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** ViewModel, gestión de la lógica de negocio y estado de UI. */
class HardwareViewModel(private val repository: LxQuestRepository) : ViewModel() {
    // Estados de la pantalla de inicio (UI).
    private val _ram = MutableStateFlow("")
    val ram = _ram.asStateFlow()
    private val _cpu = MutableStateFlow("64 bits")
    val cpu = _cpu.asStateFlow()
    private val _disco = MutableStateFlow("")
    val disco = _disco.asStateFlow()
    private val _uso = MutableStateFlow("Ofimática")
    val uso = _uso.asStateFlow()
    // Al iniciar la aplicación, se recupera el último perfil guardado (persistencia de estado).
    init {
        viewModelScope.launch {
            val usuario = repository.obtenerPerfil().firstOrNull()
            usuario?.let {
                    _ram.value = it.ram_equipo.toString()
                    _cpu.value = it.cpu_equipo
                    _disco.value = it.disco_duro.toString()
                    _uso.value = it.uso_habitual
                }
            }
        }
    // Funciones de actualización de estado.
    fun onRamChange(newValue: String) { _ram.value = newValue }
    fun onCpuChange(newValue: String) { _cpu.value = newValue }
    fun onDiscoChange(newValue: String) { _disco.value = newValue }
    fun onUsoChange(newValue: String) { _uso.value = newValue }

    // Procesar test actualizado con el nuevo campo.
    fun procesarTest() {
        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                id_usuario = 1,
                nombre_usuario = "Perfil Local",
                ram_equipo = _ram.value.toIntOrNull() ?: 0,
                cpu_equipo = _cpu.value,
                disco_duro = _disco.value.toIntOrNull() ?: 0,
                uso_habitual = _uso.value)
            repository.guardarPerfil(nuevoUsuario)
        }
    }
    // Lógica de detalle de distribuciones.
    fun obtenerDetalle(id: Int): Flow<Distribucion> {
        return repository.obtenerDetallesDistro(id)
    }
    fun obtenerDistrosFiltradas(ram: Int, disco: Int, uso: String, cpu: String): Flow<List<Distribucion>> {
        return repository.filtrarHardware(ram, disco, uso, cpu)
    }
    // Guardar notas con título y contenido dinámico.
    fun guardarNota(distroId: Int, titulo: String, contenido: String, idNotaExistente: Int = 0) {
        viewModelScope.launch {
            repository.guardarNota(
                Nota(
                    id_nota = idNotaExistente,
                    id_distro = distroId,
                    id_usuario = 1,
                    titulo = titulo,
                    contenido = contenido,
                    fecha_guardado = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())))
        }
    }
    fun obtenerNotasDistro(idDistro: Int): Flow<List<Nota>> {
        return repository.obtenerNotas(idDistro)
    }
    fun eliminarNota(nota: Nota) {
        viewModelScope.launch {
            repository.eliminarNota(nota)
        }
    }
    // Lógica de favoritos.
    fun esFavorito(idDistro: Int): Flow<Favorito?> {
        return repository.esFavorito(idDistro, 1)
    }
    fun alternarFavorito(distroId: Int, existe: Boolean) {
        viewModelScope.launch {
            // Generamos la fecha actual con el formato (dd/MM/yyyy).
            val fechaActual = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())

            if (existe) {
                repository.eliminarFavorito(distroId, 1)

            } else {
                repository.gestionarFavorito(
                    Favorito(
                        id_favorito = 0,
                        id_distro = distroId,
                        id_usuario = 1,
                        fecha_guardado = fechaActual
                    ),
                    false
                )
            }
        }
    }
    // Función para la nueva pantalla de favoritos.
    fun obtenerFavoritos(): Flow<List<Distribucion>> {
        return repository.obtenerDistrosFavoritas(1)
    }
}