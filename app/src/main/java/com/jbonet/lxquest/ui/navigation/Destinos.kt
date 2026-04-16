package com.jbonet.lxquest.ui.navigation

/** Definición de las rutas de navegación dentro de la app (Inicio, catálogo, detalle, favoritos). */
sealed class Destinos(val ruta: String) {
    data object Home : Destinos("home")
    data object Catalogo : Destinos("catalogo/{ram}/{disco}/{uso}/{cpu}") {
        fun crearRuta(ram: Int, disco: Int, uso: String, cpu: String): String {
            return "catalogo/$ram/$disco/$uso/$cpu"
        }
    }
    data object Detalle : Destinos("detalle/{id}") {
        fun crearRuta(id: Int) = "detalle/$id"
    }
    data object Favoritos : Destinos("favoritos")
}