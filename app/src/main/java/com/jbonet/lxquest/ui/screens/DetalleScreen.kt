package com.jbonet.lxquest.ui.screens

import com.jbonet.lxquest.ui.viewmodel.HardwareViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import com.jbonet.lxquest.data.entities.Nota
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Pantalla que muestra el detalla de cada distribución (ficha técnica) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(distroId: Int, viewModel: HardwareViewModel, onBack: () -> Unit) {
    // Estados desde el ViewModel usando StateFlow.
    val distro by viewModel.obtenerDetalle(distroId).collectAsState(initial = null)
    val notasGuardadas by viewModel.obtenerNotasDistro(distroId).collectAsState(initial = emptyList())
    val favoritoObj by viewModel.esFavorito(distroId).collectAsState(initial = null)

    val esFavorito = favoritoObj != null

    // Variables de estado (notas)
    var tituloNota by remember { mutableStateOf("") }
    var textoNota by remember { mutableStateOf("") }
    var notaParaEditar by remember { mutableStateOf<Nota?>(null) }
    // Control de snackbar y corrutinas.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            distro?.let { d ->
                CenterAlignedTopAppBar(
                    title = { Text(d.nombre) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    actions = {
                        // Gestión de favoritos mediante el icono de estrella.
                        IconButton(onClick = { viewModel.alternarFavorito(distroId, esFavorito) }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favorito",
                                tint = if (esFavorito) Color(0xFFFFD700) else Color.Gray)
                        }
                    })
            } ?: run {
                CenterAlignedTopAppBar(title = { Text("Cargando...") })
            }
        }
    ) { padding ->
        distro?.let { d ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .animateContentSize())
            { // Ficha técnica.
                InfoText(label = "Base", value = d.base_sistema)
                InfoText(label = "Entorno de escritorio", value = d.entorno_grafico)
                InfoText(label = "RAM mínima", value = "${d.ram_minima} GB")
                InfoText(label = "Uso recomendado", value = d.uso_recomendado)
                InfoText(label = "Ciclo de actualización", value = d.ciclo_actualizacion)
                InfoText(label = "Espacio mínimo en disco", value = "${d.espacio_disco} GB")
                InfoText(label = "Gestor de paquetes", value = d.gestor_paquetes)

                Spacer(modifier = Modifier.height(32.dp))

                // Formulario de creación / edición de notas.
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp))
                    {
                        // Título de la nota.
                        OutlinedTextField(
                            value = tituloNota,
                            onValueChange = { tituloNota = it },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            label = { Text("Título de la nota") },
                            singleLine = true)

                        // Contenido de la nota.
                        TextField(
                            value = textoNota,
                            onValueChange = { textoNota = it },
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            placeholder = { Text("Observaciones sobre la distro...") })

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End)
                        {
                            if (notaParaEditar != null) {
                                TextButton(onClick = {
                                    // Reseteo del estado de edición al cancelar.
                                    notaParaEditar = null
                                    tituloNota = ""
                                    textoNota = ""
                                }) {
                                    Text("Cancelar")
                                }
                            }
                            Button(onClick = {
                                if (textoNota.isNotBlank() && tituloNota.isNotBlank()) {
                                    viewModel.guardarNota(
                                        distroId,
                                        tituloNota,
                                        textoNota,
                                        notaParaEditar?.id_nota ?: 0)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (notaParaEditar == null) "Nota añadida" else "Nota actualizada")
                                    }
                                    // Limpieza de los campos.
                                    tituloNota = ""
                                    textoNota = ""
                                    notaParaEditar = null
                                }
                            }) {
                                Text(if (notaParaEditar == null) "Guardar nota" else "Actualizar")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Historial de notas.
                if (notasGuardadas.isNotEmpty()) {
                    Text("Notas guardadas", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

                    notasGuardadas.reversed().forEach { nota ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically)
                            { Column(modifier = Modifier.weight(1.0f)) {
                                    Text(nota.fecha_guardado, fontSize = 10.sp, color = Color.Gray)
                                    Text(nota.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
                                    Text(nota.contenido, modifier = Modifier.padding(top = 4.dp))
                                }
                                // Botón editar.
                                IconButton(onClick = {
                                    notaParaEditar = nota
                                    tituloNota = nota.titulo
                                    textoNota = nota.contenido
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                                }
                                // Botón eliminar.
                                IconButton(onClick = {
                                    scope.launch {
                                        delay(200)
                                        viewModel.eliminarNota(nota)
                                    }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
/** Muestra las filas de información de las distribuciones. */
@Composable
fun InfoText(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 18.sp))
            Text(
                text = " $value",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
        }
    }
}