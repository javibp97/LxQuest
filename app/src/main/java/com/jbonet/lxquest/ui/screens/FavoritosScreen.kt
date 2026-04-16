package com.jbonet.lxquest.ui.screens

import com.jbonet.lxquest.ui.viewmodel.HardwareViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jbonet.lxquest.data.entities.Distribucion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Muestra la pantalla con las distribuciones marcadas como favoritas. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    viewModel: HardwareViewModel,
    onNavigateToDetail: (Int) -> Unit,
    onBack: () -> Unit)
{
    val favoritos by viewModel.obtenerFavoritos().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Mis Favoritos") },
                navigationIcon = { IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        // Gestión del estado vacío de la lista de favoritos.
        if (favoritos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aún no tienes distribuciones favoritas")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .animateContentSize() // Animación al eliminar las tarjetas.
            ) {
                items(
                    items = favoritos,
                    key = { it.id_distro }
                ) { distro ->
                    FavoritoCard(
                        distro = distro,
                        viewModel = viewModel,
                        onClick = { onNavigateToDetail(distro.id_distro) })
                }
            }
        }
    }
}
// Tarjeta personalizada para la pantalla de favoritos.
@Composable
fun FavoritoCard(distro: Distribucion, viewModel: HardwareViewModel, onClick: () -> Unit) {
    val scope = rememberCoroutineScope()

    // Llamamos a la función existente para recuperar el registro de la tabla Favorito.

    val favorito by viewModel.esFavorito(distro.id_distro).collectAsState(initial = null)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            Column(modifier = Modifier.weight(1f)) {
                // Nombre de la distribución.
                Text(
                    text = distro.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
                // Fecha de guardado.
                Text(
                    text = "Guardado el: ${favorito?.fecha_guardado ?: "Cargando..."}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            // Botón para eliminar la distribución favorita.
            IconButton(onClick = {
                scope.launch {
                    delay(200) // Pequeño Delay para que el usuario perciba visualmente la acción.
                    viewModel.alternarFavorito(distro.id_distro, true)
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar favorito",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp))
            }
        }
    }
}