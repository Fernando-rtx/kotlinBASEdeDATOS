# Rodrigo — Guion palabra por palabra (3 min)

**Ficha:**
- **Tiempo total:** 3:00 minutos exactos. 0:00-1:30 teoría. 1:30-3:00 demo viva.
- **Pantalla:** Terminal a la izquierda, Chrome en `http://localhost:5984/_utils` a la derecha. Fuente terminal a 18pt. Nada más abierto.
- **Archivos que muestras:** `docker-compose.expo.yml`, `src/main/kotlin/Main.kt`, `src/main/kotlin/RodrigoModule.kt`. Repo: `Fernando-rtx/kotlinBASEdeDATOS`, rama `main`.
- **Tu rol en el orden:** Tú abres. Orden: Rodrigo (3 min) → Mario → Alberto → Fernando. Tú cierras al final pidiendo a Fernando el refresh final en Fauxton.
- **Regla de oro:** No improvises comandos. Teclea solo los de esta guía.

## Minuto 0:00-1:30 — Teoría (di esto)

> "Buenos días, soy Rodrigo y abro la exposición: Kotlin + Apache CouchDB + Docker. Mi parte es dejar la infraestructura lista y crear la base `universidad`."

[ACCIÓN EN PANTALLA: Muestra terminal vacía. No teclees aún. Mira al público.]

> "CouchDB es NoSQL orientado a documentos JSON. Cada documento tiene un `_id` único. No hay tablas ni filas. Guardamos JSON tal cual, por ejemplo un estudiante con nombre, carrera y edad."

[ACCIÓN EN PANTALLA: Levanta un dedo por cada diferencia. Sigue sin teclear.]

> "Diferencia clave con MySQL o Postgres: CouchDB no usa socket binario ni driver JDBC. Expone una API REST con JSON. Todo es HTTP: GET para leer, PUT para crear, DELETE para borrar. Cualquier cosa que hable HTTP — curl, el navegador, nuestro programa en Kotlin — opera la base."

[ACCIÓN EN PANTALLA: Abre Chrome en `http://localhost:5984/_utils`. No hagas login aún. Señala la URL.]

> "Docker nos da la expo estándar: imagen `couchdb:3`, contenedor llamado `couchdb-server`, puerto `5984`, usuario `admin` y clave `password`. Si esto corre en cualquier máquina, la demo corre. Ahora lo levanto en vivo."

[ACCIÓN EN PANTALLA: Vuelve el foco al terminal. Pon las manos en el teclado.]

## Minuto 1:30-3:00 — Demo (haz esto)

**Paso 1 — Levantar CouchDB. Teclea esto tal cual:**

```bash
docker run -d --name couchdb-server -p 5984:5984 -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password couchdb:latest
```

**Salida esperada:** ID largo del contenedor (64 caracteres hex). Si lo ves, sigue.

**Si sale `Conflict. The container name "/couchdb-server" is already in use`:**
Di: "Ya existe, lo reutilizo" y teclea:
```bash
docker start couchdb-server
```
**Salida esperada:** `couchdb-server`

**Paso 2 — Verificar que está corriendo. Teclea:**

```bash
docker ps
```

**Salida esperada:** fila con `STATUS Up`, `PORTS 0.0.0.0:5984->5984/tcp`, `NAMES couchdb-server`.

**Alternativa del repo con compose (solo si piden mostrar `docker-compose.expo.yml`):**
```bash
docker compose -f docker-compose.expo.yml up -d
```

**Paso 3 — Probar salud. Teclea:**

```bash
curl -u admin:password http://localhost:5984/
```

**Salida esperada:** `{"couchdb":"Welcome","version":"3...",...}` con `"couchdb":"Welcome"`.
Di en voz alta: "CouchDB responde".

**Si sale `curl: (7) Failed to connect`:** espera 10 segundos (CouchDB tarda en arrancar) y repite. Si sigue, `docker logs couchdb-server`.

**Si sale `401 unauthorized`:** re-teclea exacto `-u admin:password`. En expo siempre es `password`, no `fernandojose`.

**Paso 4 — Crear la base `universidad`. Teclea:**

```bash
curl -u admin:password -X PUT http://localhost:5984/universidad -w "\nHTTP:%{http_code}\n"
```

**Si es nueva:** `{"ok":true}` + `HTTP:201`. Di: "201, base creada".

**Si ya existía:** `{"error":"file_exists",...}` + `HTTP:412`. Di: "412, ya existía, la reutilizamos. No es error, es lo esperado".

**Paso 5 — Listar bases. Teclea:**

```bash
curl -s -u admin:password http://localhost:5984/_all_dbs
```

**Salida esperada:** lista con `"universidad"`. Señálala con el cursor.

**Paso 6 — Fauxton clic por clic:**
1. Ve a `http://localhost:5984/_utils`.
2. Usuario `admin`, clave `password`, clic `Log In`.
3. Panel `Databases`, clic en `universidad`.
4. Di: "Aquí vivirá el documento `est_2026_01` que creará Mario".
5. Deja la pestaña abierta. Al final pedirás el refresh aquí.

## Código línea por línea

Abre `src/main/kotlin/RodrigoModule.kt` y señala cada línea:

```kotlin
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object RodrigoModule {
    fun crearBaseDatos() {
        println("[Rodrigo] Verificando e inicializando base de datos '$DB_NAME'...")
        val request = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME")
            .put("".toRequestBody())
            .header("Authorization", AUTH_CREDENTIALS)
            .build()
        httpClient.newCall(request).execute().use { response ->
            when (response.code) {
                201 -> println("[Rodrigo] Base de datos creada con exito (HTTP 201).")
                412 -> println("[Rodrigo] La base de datos ya existia previamente (HTTP 412).")
                else -> println("[Rodrigo] Respuesta inesperada del servidor: HTTP ${response.code}")
            }
        }
    }
}
```

- `import okhttp3.Request` + `toRequestBody`: cliente HTTP OkHttp, sin driver, solo HTTP.
- `object RodrigoModule`: singleton de Kotlin.
- `fun crearBaseDatos()`: hace `PUT /universidad`.
- `.url("$COUCHDB_URL/$DB_NAME")`: = `http://localhost:5984/universidad` (constantes de `Main.kt`).
- `.put("".toRequestBody())`: PUT con cuerpo vacío = crear base en CouchDB.
- `.header("Authorization", AUTH_CREDENTIALS)`: `Basic` de `admin/password` (calculado en `Main.kt` con `Credentials.basic`).
- `when (response.code)`: 201 creada, 412 ya existía, else depurar.

En `Main.kt` señala:
```kotlin
val httpClient = OkHttpClient()
const val COUCHDB_URL = "http://localhost:5984"
const val DB_NAME = "universidad"
val AUTH_CREDENTIALS = Credentials.basic("admin", "password")
```
Di: "Estas cuatro líneas son mi contrato con Mario, Alberto y Fernando: misma URL, misma base, misma auth, mismo cliente".
Y su llamada primera en `main`: `RodrigoModule.crearBaseDatos()`. Di: "Mi función corre primera. Sin `universidad`, nada de lo que sigue funciona".

## Transición a Mario

Mira a Mario, señala Fauxton en `universidad` y di literal:

> "Con la base `universidad` lista y respondiendo 201 o 412, le paso a Mario, que conecta Kotlin con OkHttp y crea el primer documento."

## Si algo falla

**1. `Connection refused`:** `docker ps`; si no está Up, `docker start couchdb-server`, espera 15 s, repite curl. Si log dice `address already in use`, mata el proceso en 5984 y repite.
**2. `401 unauthorized`:** re-teclea `-u admin:password`; revisa `Credentials.basic("admin", "password")`; si el contenedor se creó con otra clave, `docker rm -f couchdb-server` y re-run estándar.
**3. `412` en PUT o puerto ocupado:** 412 = seguimos, no borres. Puerto ocupado = reutiliza el contenedor existente, no cambies el puerto (`Main.kt` espera 5984).

**Frase final que guardas para el cierre:** "Fernando, haz refresh en Fauxton en `universidad` para confirmar que el documento ya no está."
