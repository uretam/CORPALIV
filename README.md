# CORPALIV · Alba Lab

Aplicación Android de la Evaluación Parcial 3 (DSY1105). Presenta el propósito de CORPALIV y Alba Lab, muestra formas de colaboración y registra intereses de contacto y capacitación en el dispositivo.

**Equipo:** Maximiliano Ureta Morales y Francisco Ramírez Medina.

## Funciones

- Interfaz Kotlin + Jetpack Compose + Material 3, con menú lateral y navegación sencilla.
- Dos formularios con validación en ViewModel.
- Crear, consultar, editar y eliminar registros locales con Room.
- Cámara, compartir y notificación local; animación de confirmación.

## Ejecutar

Abre la carpeta raíz (contiene `settings.gradle.kts`) en Android Studio Ladybug 2024.2.1 o posterior. Usa JDK 17 o superior, instala Android SDK 35, espera Gradle Sync y ejecuta `app` en Android 8.0 (API 26) o superior. El proyecto usa el Gradle Wrapper 8.10.2. La primera sincronización necesita internet.

**Recorrido recomendado:** Inicio → Colaborar → Contacto → Registrar interés → Registros. Abre Menú para llegar también a Capacitación. Los formularios se guardan localmente; no envían información a un servidor. Para la demostración usa datos de prueba.
