package com.jbonet.lxquest.ui.components

import com.jbonet.lxquest.data.entities.Distribucion
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.jbonet.lxquest.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.Icons

/** Diseño para representar las distribuciones dentro de Catálogo. */
@Composable
fun DistroItem(distro: Distribucion, onClick: () -> Unit) {

    val orangeAccent = Color(0xFFFF9800)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {onClick()},
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface))
    {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_linux),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp))

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = distro.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Basado en ${distro.base_sistema}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ver detalles de ${distro.nombre}", // Ajuste de accesibilidad para usuarios con visión reducida.
                tint = MaterialTheme.colorScheme.outline)
        }
    }
}