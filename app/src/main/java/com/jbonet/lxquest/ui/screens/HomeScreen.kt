package com.jbonet.lxquest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jbonet.lxquest.ui.navigation.Destinos
import com.jbonet.lxquest.ui.viewmodel.HardwareViewModel


/** Pantalla de inicio con el formulario sobre el hardware. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HardwareViewModel)
{
    val ram by viewModel.ram.collectAsState()
    val cpu by viewModel.cpu.collectAsState()
    val disco by viewModel.disco.collectAsState()
    val uso by viewModel.uso.collectAsState()

    // Opciones por defecto para los menús desplegables (Arquitectura CPU y Uso recomendado).
    val opcionesUso = listOf("Ofimática", "Gaming", "Desarrollo")
    val opcionesCpu = listOf("64 bits", "32 bits")
    // Estados para controlar los menús desplegables (Arquitectura CPU y Uso recomendado).
    var expandedCpu by remember { mutableStateOf(false) }
    var expandedUso by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(24.dp))
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            // Título y acceso directo a la pantalla Favoritos.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically)
            {
                Spacer(modifier = Modifier.width(48.dp))
                Text("LxQuest", style = MaterialTheme.typography.headlineMedium)

                IconButton(onClick = { navController.navigate("favoritos") })
                {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favoritos",
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFFFFD700))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de texto para la RAM mínima.
            CustomHardwareInput(
                label = "RAM mínima (GB)",
                value = ram,
                helperText = "(1GB en adelante)",
                onValueChange = { viewModel.onRamChange(it) })

            // Menú desplegable para la arquitectura de la CPU.
            ExposedDropdownMenuBox(
                expanded = expandedCpu,
                onExpandedChange = { expandedCpu = !expandedCpu },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
            {
                OutlinedTextField(
                    value = cpu,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Arquitectura de CPU") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCpu) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth())
                ExposedDropdownMenu(
                    expanded = expandedCpu,
                    onDismissRequest = { expandedCpu = false })
                {
                    opcionesCpu.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                viewModel.onCpuChange(opcion)
                                expandedCpu = false
                            }
                        )
                    }
                }
            }
            // Menú desplegable para el uso recomendado.
            ExposedDropdownMenuBox(
                expanded = expandedUso,
                onExpandedChange = { expandedUso = !expandedUso },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
            {
                OutlinedTextField(
                    value = uso,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Uso recomendado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUso) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth())
                ExposedDropdownMenu(
                    expanded = expandedUso,
                    onDismissRequest = { expandedUso = false })
                {
                    opcionesUso.forEach { opcion ->
                        DropdownMenuItem(
                            text = {Text(opcion)},
                            onClick = {
                                viewModel.onUsoChange(opcion)
                                expandedUso = false
                            }
                        )
                    }
                }
            }
            // Campo de texto para el espacio mínimo en disco.
            CustomHardwareInput(
                label = "Espacio mínimo en disco (GB)",
                value = disco,
                helperText = "(1 GB en adelante)",
                onValueChange = { viewModel.onDiscoChange(it) })

            Spacer(modifier = Modifier.height(100.dp))
        }
        // Botón Buscar distribuciones: Navega hacia la pantalla Catálogo.
        Button(
            onClick = {
                viewModel.procesarTest()
                navController.navigate(Destinos.Catalogo.crearRuta(
                    ram.toIntOrNull() ?: 0,
                    disco.toIntOrNull() ?: 0,
                    uso,
                    cpu))
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            // El botón se habilita solo si los campos están rellenos.
            enabled = ram.isNotBlank() && disco.isNotBlank() && uso.isNotBlank())
        {
            Text("Buscar distribuciones")
        }
    }
}
/** Componente de entrada para valores numéricos del hardware */
@Composable
fun CustomHardwareInput(label: String, value: String, helperText: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            // Forzar teclado numérico.
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
            trailingIcon = {
                if (value.isNotBlank()) {
                    IconButton(onClick = { onValueChange("")}) {
                        Icon(Icons.Default.Close, contentDescription = "Limpiar")
                    }
                }
            }
        )
        Text(
            text = helperText,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            color = Color.Gray)
    }
}