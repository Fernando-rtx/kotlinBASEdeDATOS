# Mario — Kotlin, Auth y CREATE (3.5 min)

## Objetivo
Mostrar JVM/Gradle/OkHttp, la autenticación y crear `est_2026_01` (nace `rev 1-...`).

## Guion
1. (1 min) "Kotlin corre en la JVM: rendimiento + librerías Java. Deps en `build.gradle.kts`: `okhttp:4.12.0`, `json:20240303`."
2. (1 min) "CouchDB valida cada request. Header `Authorization: Basic ...` con `Credentials.basic(\"admin\",\"password\")`. Sin ella → 401."
3. (1.5 min) Ejecutar `MarioModule.crearDocumento`, mostrar 201 + rev.

## Demo
```bash
./gradlew run
```
Salida:
```
[Mario] Creando nuevo documento con ID: 'est_2026_01'...
[Mario - CREATE] Documento registrado exitosamente.
 -> Codigo HTTP: 201
 -> Revision inicial asignada: 1-abc...
```
Verificar en Fauxton que aparece `est_2026_01` (Juan Perez).

## Si da 409 en vez de 201
La seed ya lo creó. Explica "ya existía" y sigue con el GET. Para directo limpio, borrar antes (ver README).

## Transición
"...el documento ya está persistido, le paso a **Alberto**, que lo lee y lo consulta."
