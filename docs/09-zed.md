# Trabajar con Zed (Fernando y quien use Zed)

Zed no tiene botón Run de Gradle como IntelliJ: se compila y corre desde su terminal integrada.

## Abrir el proyecto
1. `zeditor ~/kotlinBASEdeDATOS` (o Abrir carpeta en Zed).
2. Instala la extensión **Kotlin** en Zed (para sintaxis; el que compila es Gradle, no el editor).
3. Abre la terminal integrada (`` Ctrl+` ``).

## Compilar y correr (en la raíz, donde está `build.gradle.kts`)
```bash
./gradlew compileKotlin   # verifica (usa Gradle 9 + JDK 21 auto-descargado)
./gradlew run             # demo completa: Rodrigo→Mario→Alberto→Fernando
```

## Si `./gradlew` dice `gradle-wrapper.jar` no encontrado
El repo no incluye el jar (binario). Tres arreglos, en orden:
1. **Copiarlo de otro lado** (si un compañero ya lo tiene en `gradle/wrapper/`).
2. **Regenerarlo** (si tienes Gradle instalado): `gradle wrapper --gradle-version 9.0.0`.
3. **Instalar Gradle** (Arch): `sudo pacman -S gradle` (9.x, sirve igual) y usar `gradle run` directo.

## Dónde mirar tu parte
- Tu código: `src/main/kotlin/FernandoModule.kt`.
- Problemas del editor (`unresolved reference`) se ignoran: lo que manda es `./gradlew compileKotlin` → `BUILD SUCCESSFUL`.
- Todo el `System.out` de `./gradlew run` es lo que proyectas en la expo.
