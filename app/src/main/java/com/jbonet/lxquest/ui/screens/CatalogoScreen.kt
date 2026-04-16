package com.jbonet.lxquest.ui.screens

import androidx.compose.runtime.Composable
import com.jbonet.lxquest.data.entities.Distribucion
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.jbonet.lxquest.ui.components.DistroItem

/** Pantalla que muestra el listado de distribuciones compatibles con los requisitos de hardware indicados. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    distribuciones: List<Distribucion>,
    onDistroClick: (Int) -> Unit,
    onBack: () -> Unit)
{ Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (distribuciones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center)
            { Text("No hay distribuciones que coincidan con tu búsqueda")
            }
        } else {
            // Diseño del listado de resultados.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp))
            {
                items(
                    items = distribuciones,
                    key = {it.id_distro}  // Mejora de las animaciones al navegar entre las pantallas.
                ) { distro -> DistroItem(
                    distro = distro,
                    onClick = { onDistroClick(distro.id_distro) })
                }
            }
        }
    }
}