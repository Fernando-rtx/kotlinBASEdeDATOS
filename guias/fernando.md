# Fernando — Guion palabra por palabra (4 min, cierras la expo)

**Ficha:** FERNANDO, 4 de 4, 4 min. Repo `Fernando-rtx/kotlinBASEdeDATOS` `main`. Base `universidad`, doc `est_2026_01`. Expo `admin/password` (casa: `fernandojose`). Archivo `FernandoModule.kt`: `actualizarDocumento()` + `eliminarDocumento()`. Recibes `revBase` de Alberto; dejas tombstone + `DEMOSTRACION FINALIZADA CON EXITO`. Pantalla: IntelliJ + Fauxton en `universidad`.

## Minuto 0:00-1:30 — MVCC (di esto, sin ejecutar)

Señala en Fauxton el `_rev` `1-...` de `est_2026_01` y di:

> "Gracias Alberto. Soy Fernando y cierro con el Paso 4: concurrencia MVCC, actualización y borrado."
> "CouchDB no bloquea tablas ni filas como MySQL. Usa MVCC: control de concurrencia multiversión."
> "Cada documento tiene cadena de revisiones. Ahora `est_2026_01` está en revisión 1 (`1-guion`, mírenla en Fauxton)."
> "Para modificarlo es obligatorio mandar el `_rev` vigente. Rev vieja, vacía o nula → `409 conflict`, sin sobrescribir. Así dos personas no se pisan sin bloquearse."
> "Lo demuestro: actualizo con la buena y nace la `2-guion`; repito con la vieja y fuerzo el 409; borro con la `2-guion`."

Abre `FernandoModule.kt`, señala `put("_rev", revActual)`: "Esta línea es la clave. Sin `_rev` no hay update. Ahora lo ejecuto."

## Minuto 1:30-3:00 — UPDATE + 409 (haz esto)

> "Hago PUT a `/universidad/est_2026_01` con `_rev` vigente, `Juan Perez Actualizado`, `Ingenieria de Sistemas`, 23."

Salida vigente (`2-...`, el hash cambia por máquina):
```
[Fernando] Actualizando documento bajo control de versiones MVCC...
[Fernando - UPDATE] Documento actualizado correctamente.
 -> Codigo HTTP: 201
 -> Nueva revision generada: 2-abc123
```
> "201 y nació la 2. La 1 murió. Con la 1 ya falla."

Fuerza el 409 (segunda terminal):
```bash
U=admin:password; C=http://localhost:5984
REV1=$(curl -s -u $U $C/universidad/est_2026_01 | python3 -c "import json,sys; print(json.load(sys.stdin)['_rev'])")
REV_VIEJA="1-xxxxxxxxxx"
curl -s -u $U -X PUT $C/universidad/est_2026_01 -H "Content-Type: application/json" -d "{\"_rev\":\"$REV_VIEJA\",\"nombre\":\"Obsoleto\",\"carrera\":\"Ingenieria de Sistemas\",\"edad\":99}" -w "\nHTTP:%{http_code}\n"
```
```json
{"error":"conflict","reason":"Document update conflict."}
HTTP:409
```
En Kotlin sale: `[Fernando - UPDATE] Conflicto 409 detectado: La revision proporcionada es invalida.`
> "409: rechazado por obsoleto, sin sobrescribir ni bloquear. El dato bueno (edad 23) sigue intacto."

Respaldo UPDATE vigente por curl:
```bash
curl -s -u $U -X PUT $C/universidad/est_2026_01 -H "Content-Type: application/json" -d "{\"_rev\":\"$REV1\",\"nombre\":\"Juan Perez Actualizado\",\"carrera\":\"Ingenieria de Sistemas\",\"edad\":23}"
# -> {"ok":true,"id":"est_2026_01","rev":"2-..."}
```
Guarda la `2-...` en papel para el DELETE.

## Minuto 3:00-4:00 — DELETE + cierre

> "Cierro el CRUD: para borrar también es obligatorio mandar la revisión vigente, la `2-guion`. Sin rev no hay borrado."

```
[Fernando] Eliminando documento proporcionando token de revision...
[Fernando - DELETE] Documento eliminado satisfactoriamente.
 -> Codigo HTTP: 200
 -> Respuesta de CouchDB: {"ok":true,"id":"est_2026_01","rev":"3-..."}
```
> "200 `ok true`: no borra físico, deja tombstone `_deleted:true` con rev `3-guion`."

Comprueba el 404:
```bash
curl -s -u $U $C/universidad/est_2026_01 -w "\nHTTP:%{http_code}\n"
# {"error":"not_found","reason":"deleted"} HTTP:404
```
Mira a Rodrigo: > "Rodrigo, por favor refresca Fauxton ahora."
Tras F5 (`est_2026_01` desaparece): > "404 deleted, en Fauxton ya no está. Ciclo crear-leer-actualizar con MVCC-borrar completo, sin bloqueos y con historial."
Terminal final:
```
====================================================
 DEMOSTRACION FINALIZADA CON EXITO
====================================================
```

## Código línea por línea

`actualizarDocumento(id, revActual, nuevoNombre, nuevaEdad): String`: recibe ej. `est_2026_01`, `1-a1b2`, `Juan Perez Actualizado`, 23; devuelve `2-...` (o `""` con 409). `put("_rev", revActual)` obligatorio; `nombre/carrera/edad` datos (carrera fija para Mango de Alberto). `PUT .../universidad/est_2026_01` + Auth. `if 409 → imprime conflicto, return ""` (`Main.kt` evita borrar con `if (revisionV2.isNotEmpty())`). Si no: `optString("rev")`, imprime 201 + rev, la retorna.

`eliminarDocumento(id, revParaEliminar)`: ej. `est_2026_01`, `2-abc`. Rev va en URL `?rev=` (no en cuerpo), `.delete()`. Imprime 200 + `{"ok":true}` = tombstone rev `3-...`.

`Main.kt`: `val revisionV2 = FernandoModule.actualizarDocumento(...revBase...)` + `if (revisionV2.isNotEmpty()) FernandoModule.eliminarDocumento(documentoId, revisionV2)`.

## Cierre literal

> "DEMOSTRACION FINALIZADA CON EXITO. CRUD completo en Kotlin con CouchDB y Docker, sin bloqueos, con MVCC y borrado lógico. Gracias."

## Si algo falla

- **409 al primer intento:** `revBase` vacía o reusada. `curl .../_all_docs?` GET del doc, copia `_rev` vigente, repite PUT. Di: "Recupero la vigente y repito."
- **404 `no_db_file`/`missing`:** falta base o doc. `PUT /universidad`, luego `PUT /est_2026_01` base (Juan Perez/22), repite UPDATE.
- **400 `Invalid rev format`:** rev copiada con espacios/comillas. `python3 -c "print(repr(...['_rev']))"`, copia exacta `N-hash`.
- **401:** en casa usa `admin:fernandojose` en los curls de respaldo.
- **Nada sale:** muestra `docs/04-fernando.md` y di: "Sin `_rev` vigente hay 409, con vigente hay 201 y nace `2-guion`, DELETE con `?rev=` deja tombstone y 404." Ejecuta los 3 curls en orden y cierra igual.
