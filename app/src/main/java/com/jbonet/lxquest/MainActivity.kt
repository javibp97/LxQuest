package com.jbonet.lxquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jbonet.lxquest.ui.navigation.Destinos
import com.jbonet.lxquest.ui.screens.HomeScreen
import com.jbonet.lxquest.ui.screens.CatalogoScreen
import com.jbonet.lxquest.ui.screens.DetalleScreen
import com.jbonet.lxquest.ui.viewmodel.HardwareViewModel
import com.jbonet.lxquest.ui.screens.FavoritosScreen

/** Punto de entrada de la aplicación. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Accedemos a la instacia de la app y a su repositorio.
        val app = application as LxQuestApplication
        val viewModel = HardwareViewModel(app.repository)

        setContent {
            // navController para la navegación entre pantallas.
            val navController = rememberNavController()

            MaterialTheme {
                NavHost(
                    navController = navController,
                    startDestination = Destinos.Home.ruta)
                {
                    // Pantalla de inicio.
                    composable(Destinos.Home.ruta) {
                        HomeScreen(navController = navController, viewModel = viewModel)
                    }
                    // Pantalla de Catálogo.
                    composable(
                        route = Destinos.Catalogo.ruta,
                        arguments = listOf(
                            navArgument("ram") { type = NavType.IntType },
                            navArgument("disco") { type = NavType.IntType },
                            navArgument("uso") { type = NavType.StringType },
                            navArgument("cpu") { type = NavType.StringType }))
                    { backStackEntry ->
                        val ram = backStackEntry.arguments?.getInt("ram") ?: 0
                        val disco = backStackEntry.arguments?.getInt("disco") ?: 0
                        val uso = backStackEntry.arguments?.getString("uso") ?: ""
                        val cpu = backStackEntry.arguments?.getString("cpu") ?: "64 bits"

                        val listaFiltrada by viewModel.obtenerDistrosFiltradas(ram, disco, uso, cpu)
                            .collectAsState(initial = emptyList())
                        CatalogoScreen(
                            distribuciones = listaFiltrada,
                            onDistroClick = { id ->
                                navController.navigate(Destinos.Detalle.crearRuta(id)) },
                            onBack = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                    // Pantalla de Detalle.
                    composable(
                        route = Destinos.Detalle.ruta,
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        DetalleScreen(
                            distroId = id,
                            viewModel = viewModel,
                            onBack = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                    // Pantalla de Favoritos.
                    composable(Destinos.Favoritos.ruta) {
                        FavoritosScreen(
                            viewModel = viewModel,
                            onNavigateToDetail = { id ->
                                navController.navigate(Destinos.Detalle.crearRuta(id))
                            },
                            onBack = {
                                if (navController.previousBackStackEntry != null)
                                {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}