# Mario — Guion palabra por palabra (3.5 min)

**Ficha:** MARIO, Paso 2, 3.5 min. Orden: Rodrigo → MARIO → Alberto → Fernando.
Repo `Fernando-rtx/kotlinBASEdeDATOS` rama `main`. Archivos: `build.gradle.kts`, `Main.kt`, `MarioModule.kt`.
Demuestras: Kotlin JVM + Gradle + Auth Basic + CREATE `PUT /universidad/est_2026_01` → 201 + `rev 1-...`.
Dato previo: Rodrigo ya creó `universidad`. Tú creas el documento `est_2026_01`.

## Minuto 0:00-1:00 — Gradle (di esto)

> "Soy Mario, paso dos. Mi parte es Kotlin, la autenticación y el CREATE."
> "Kotlin corre en la JVM. Eso nos da rendimiento y todas las librerías de Java."
> "No usamos REST a mano. Usamos dos dependencias en `build.gradle.kts`: OkHttp para HTTP y JSON-java para el documento."
> "Miren, son exactamente estas dos líneas, sin más."

[ACCIÓN: mostrar build.gradle.kts y señalar:]
```kotlin
dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20240303")
}
```

> "OkHttp 4.12.0 hace la petición PUT. JSON 20240303 construye el cuerpo. El proyecto es `kotlin jvm 2.2.0` con `jvmToolchain(21)` y clase principal `MainKt`. Eso es todo lo que Gradle necesita."

## Minuto 1:00-2:00 — Auth (di esto)

> "CouchDB valida cada request. Sin autenticación devuelve 401 Unauthorized."
> "Usamos Basic Auth con `admin` / `password`. En código es una sola constante."
> "Esa constante va en cada request en el header `Authorization`. Sin ese header, nada funciona."

[ACCIÓN: en `Main.kt` señalar:]
```kotlin
val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()
val httpClient = OkHttpClient()
const val COUCHDB_URL = "http://localhost:5984"
const val DB_NAME = "universidad"
val AUTH_CREDENTIALS = Credentials.basic("admin", "password")
```
> "`Credentials.basic(\"admin\",\"password\")` genera `Authorization: Basic ...`. Coincide con el Docker: `-e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password`."

[ACCIÓN: en `MarioModule.kt` señalar `.header("Authorization", AUTH_CREDENTIALS)`]
> "Esta línea pone el header en el PUT. Sin ella → 401. Con ella → crear. Ahora lo demuestro."

## Minuto 2:00-3:30 — CREATE (haz esto)

**Paso 1 — Muestra qué vas a crear.** En `Main.kt` señala:
```kotlin
val documentoId = "est_2026_01"
val revisionV1 = MarioModule.crearDocumento(id = documentoId, nombre = "Juan Perez", carrera = "Ingenieria de Sistemas", edad = 22)
```
> "Voy a crear `est_2026_01` con PUT a `http://localhost:5984/universidad/est_2026_01` y cuerpo `{\"nombre\":\"Juan Perez\",\"carrera\":\"Ingenieria de Sistemas\",\"edad\":22}`. Si sale bien: 201 + revisión `1-`."

**Paso 2 — Ejecuta** (`./gradlew run` o botón Run en `Main.kt`).

**Paso 3 — Lee la salida en voz alta:**
```
[Mario] Creando nuevo documento con ID: 'est_2026_01'...
[Mario - CREATE] Documento registrado exitosamente.
 -> Codigo HTTP: 201
 -> Revision inicial asignada: 1-abc...
```
> "Creando `est_2026_01`. Registrado. HTTP 201 = creado. Revisión `1-...`: la `1` es versión uno." (El hash cambia por máquina, normal.)

**Paso 4 — Verifica en Fauxton** (`http://localhost:5984/_utils`, `universidad`, doc `est_2026_01` con Juan Perez / Ingenieria de Sistemas / 22).
> "Aquí está en Fauxton. El CREATE funcionó."

**Plan B si sale 409:** di sin nervios:
> "Me dio 409: el documento ya existía por la seed. No es error mío, CouchDB protege el dato. El código ya lo prevé."
Señala en `Main.kt`: `val revParaLeer = revisionV1.ifEmpty { AlbertoModule.obtenerRevision(documentoId) }`.
> "Si la revisión viene vacía por el 409, pedimos la vigente y Alberto lee igual."

## Código línea por línea (`MarioModule.kt`)

```kotlin
fun crearDocumento(id: String, nombre: String, carrera: String, edad: Int): String {
    val docJson = JSONObject().apply { put("nombre", nombre); put("carrera", carrera); put("edad", edad) }
    val request = Request.Builder()
        .url("$COUCHDB_URL/$DB_NAME/$id")
        .put(docJson.toString().toRequestBody(JSON_MEDIA))
        .header("Authorization", AUTH_CREDENTIALS)
        .build()
    httpClient.newCall(request).execute().use { response ->
        val revisionGenerada = JSONObject(response.body?.string() ?: "{}").optString("rev")
        println(...201...); println(...rev...)
        return revisionGenerada
    }
}
```
- Firma: recibe id/nombre/carrera/edad, devuelve `rev`.
- `JSONObject` sin `_id`/`_rev`: el ID va en la URL (por eso PUT con ID, no POST).
- `.url(...)`: = `http://localhost:5984/universidad/est_2026_01`.
- `.put(...toRequestBody(JSON_MEDIA))`: JSON con `application/json; charset=utf-8`.
- `.header(...)`: Basic admin/password, sin esto 401.
- `optString("rev")`: no revienta con 409, devuelve vacío.
- `return revisionGenerada`: `Main.kt` la usa para UPDATE/DELETE de Fernando.

## Transición a Alberto

> "El documento ya está persistido, le paso a Alberto, que lo lee y lo consulta."

## Si algo falla

- **401:** verifica `Credentials.basic("admin", "password")` y login Fauxton. En casa de Fernando usa `fernandojose`.
- **409:** explica y sigue (ver Plan B). Directo limpio = borrar doc y re-correr para el 201.
- **No compila:** corre en la raíz (`build.gradle.kts`), revisa JDK 21 (`jvmToolchain(21)`), `./gradlew --refresh-dependencies run`.
