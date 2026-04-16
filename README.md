**LxQuest**

Asistente inteligente y gestión de distribuciones Linux para Android.

1. Manual de usuario:

LxQuest permite al usuario navegar a través del catálogo de distribuciones que su equipo requiera:

-- Test de hardware: Introduce mediante el teclado numérico/menú desplegable los datos de tu equipo (RAM, Arquitectura CPU, Espacio en disco) para recibir recomendaciones personalizadas.

-- Ficha técnica: Visualización de los detalles de cada distribución elegida (Base del sistema, Acceso a detalles específicos de cada distro (Base, Entorno de escritorio, RAM mínima, uso recomendado, ciclo de actualización, espacio mínimo en disco y gestor de paquetes).

-- Favoritos: Dentro de la ficha de detalles, indica sus distribuciones favoritas para consultarlas desde el menú principal.

-- Notas: Añadir notas a cada distribución para facilitar la guía de instalación.

2. Manual de instalación:

Para la ejecución de LxQuest en un entorno de desarrollo, debe seguir estos pasos:

-- Requisitos previos:
**Android Studio**:
**JDK 17 (o superior).**
**SDK de Android: API 26(Android 8.0) como mínimo.**

Pasos para obtener el proyecto:
1. Clonar el repositorio: https://github.com/javibp97/LxQuest.git
2. Importar el proyecto: Ubicar la carpeta raíz del proyecto y abirla dentro de Android Studio.
3. Sincronizar Gradle: Esperar el tiempo suficiente para que el IDE descargue cada una de las dependencias (Room, Jetpack Compose, etc.)
4. Ejecutar: Puede ejecutar la aplicación mediante unn dispositivo físico (Android) o iniciar un emulador, posteriormente pulsar el botón Run app que se encuentra en la barra superior.

Nota: El proyecto ha sido creado con una base de datos SQLite gestionada a través de Room, por lo tanto, no requiere una configuración de servidores externos.
