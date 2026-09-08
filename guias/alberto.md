# Alberto — Guion palabra por palabra (3.5 min)

**Ficha:** ALBERTO, 3.º (Rodrigo → Mario → ALBERTO → Fernando), 3.5 min.
Archivos: `AlbertoModule.kt`, `Main.kt`. Base `universidad`, docs `est_2026_01/02/03`. Auth `admin/password`.
Demuestras: `GET /universidad/est_2026_01` O(1) + `POST /universidad/_find` Mango + parseo `JSONObject` + helper `obtenerRevision`.
Lleva abierto: IntelliJ (`AlbertoModule.kt`, `Main.kt`), terminal con Docker, Fauxton.

## Minuto 0:00-1:00 — GET (di y haz)

> "Soy Alberto y me toca lectura. Mario ya creó `est_2026_01`. Lo leo por clave primaria: en CouchDB es búsqueda directa en el B-Tree, O(1), sin escanear. Es un `GET` a `/universidad/est_2026_01`. Miren la consola, sale 200 con el payload."

1. En `Main.kt` señala `AlbertoModule.leerDocumentoPorId(documentoId)`.
2. En `AlbertoModule.kt` señala `leerDocumentoPorId` (`.get()` + `Authorization` + `httpClient`).
3. Ejecuta (Run en `Main.kt`). Respaldo terminal:
```bash
curl -s -u admin:password http://localhost:5984/universidad/est_2026_01 | python3 -m json.tool
```
Salida:
```
[Alberto] Leyendo documento por clave primaria (GET)...
[Alberto - READ] Codigo HTTP: 200
 -> Payload recibido: {"_id":"est_2026_01","_rev":"1-...","nombre":"Juan Perez","carrera":"Ingenieria de Sistemas","edad":22}
```
> "Ven 200 = éxito, con `_id`, `_rev`, `nombre`, `carrera`, `edad`. El `_rev` lo necesitará Fernando. GET trae todo el doc, no deja pedir campos."

## Minuto 1:00-3:00 — Mango (di y haz)

> "Ahora filtrar por carrera. Con GET no se puede (solo ID). Sin MapReduce JS usamos Mango: consulta declarativa con JSON, un `POST` a `/universidad/_find`."

Muestra este JSON y léelo campo por campo:
```json
{"selector": {"carrera": {"$eq": "Ingenieria de Sistemas"}}, "fields": ["_id", "nombre", "carrera", "edad"]}
```
> "`selector` = condición (WHERE). `carrera` = campo. `$eq` = igual exacto: `Ingenieria de Sistemas` con 'de', sin tilde, I y S mayúsculas o no matchea. `fields` = proyección: solo esos 4, sin `_rev`, ahorra red."

Señala en `AlbertoModule.kt`:
```kotlin
val queryMango = JSONObject().apply {
    put("selector", JSONObject().apply { put("carrera", JSONObject().put("\$eq", carreraFiltro)) })
    put("fields", JSONArray(listOf("_id", "nombre", "carrera", "edad")))
}
```
> "Lo armo con `JSONObject/JSONArray`, no a mano. Se manda con `.post(...toRequestBody(JSON_MEDIA))` a `.../universidad/_find`."

Curl equivalente:
```bash
curl -s -u admin:password -X POST http://localhost:5984/universidad/_find -H "Content-Type: application/json" -d '{"selector":{"carrera":{"$eq":"Ingenieria de Sistemas"}},"fields":["_id","nombre","carrera","edad"]}' | python3 -m json.tool
```
Salida (10 seg en pantalla):
```
[Alberto] Ejecutando consulta declarativa Mango (_find)...
[Alberto - MANGO QUERY] Codigo HTTP: 200
 -> Registros coincidentes: {"docs":[{"_id":"est_2026_01",...Juan...},{"_id":"est_2026_02",...Maria...}]}
```
> "Devuelve 2: `est_2026_01` y `est_2026_02`, ambos de Sistemas. Excluye `est_2026_03` (Medicina). Cada doc trae solo los 4 `fields`."

## Minuto 3:00-3:30 — Parseo

> "Lo que llega es `String`. Lo imprimo crudo con `response.body?.string()`. Para usarlo lo envuelvo: `JSONObject(response.body?.string() ?: \"{}\")` y ya puedo hacer `.optString(\"_rev\")` o recorrer `JSONArray`. Mi `obtenerRevision` hace GET silencioso y devuelve el `_rev`: si Mario chocó con 409, `Main.kt` la rescata así: `val revParaLeer = revisionV1.ifEmpty { AlbertoModule.obtenerRevision(documentoId) }`. Sin ese `_rev`, Fernando no puede hacer nada."

## Código línea por línea

`leerDocumentoPorId`: `.url(.../$id)` = `.../est_2026_01`; `.get()` lectura O(1); header sin esto 401; `body?.string()` se lee una sola vez.
`consultarPorCarrera`: `$` en Kotlin se escapa `\"$eq\"`; `JSONArray` de campos; siempre `POST` a `/_find` con `JSON_MEDIA`; respuesta `{"docs":[...]}`.
`obtenerRevision`: helper silencioso; si no 200 devuelve `""`; `optString("_rev")` no revienta.

## Transición a Fernando

> "Ya demostré leer por ID en O(1) y filtrar por carrera con Mango. Le paso a **Fernando**, que actualiza y borra con MVCC usando el `_rev`."

## Si algo falla

- **GET 404:** `{"error":"not_found"}` = doc no existe. Di que lo recreas (Fauxton New Doc o `MarioModule.crearDocumento`) y repites.
- **`docs:[]` con 200:** filtro sin match; revisa `Ingenieria de Sistemas` carácter por carácter.
- **`warning: No matching index`:** normal sin índice, igual devuelve; se arregla con `POST /_index` pero no en vivo.
