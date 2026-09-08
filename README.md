# Kotlin + Apache CouchDB + Docker — Expo completa

Demo en vivo: app Kotlin/JVM (OkHttp) habla con CouchDB 3.x en Docker por REST/JSON.
Sin JDBC, sin driver pesado: `GET/PUT/POST/DELETE` + Basic Auth + MVCC con `_rev`.

## Equipo y orden (14 min total aprox)
| # | Quién | Bloque | Tiempo | Entrega en vivo |
|---|-------|--------|--------|-----------------|
| 1 | Rodrigo | Infra, Docker, fundamentos CouchDB | 3 min | `docker run`, `curl`, Fauxton, `PUT /universidad` 201/412 |
| 2 | Mario | Kotlin/Gradle, Auth, CREATE | 3.5 min | `build.gradle.kts`, `Credentials.basic`, `PUT /universidad/est_2026_01` 201 + `rev 1-...` |
| 3 | Alberto | READ por ID + Mango `_find` | 3.5 min | `GET` O(1) + `POST _find` con `$eq`, parseo `JSONObject` |
| 4 | Fernando | MVCC, UPDATE 409, DELETE tombstone | 4 min | `PUT` con `_rev` → `2-...`, `409` con rev vieja, `DELETE ?rev=` → `200` |

Minuta de show: `docs/00-orden-expo.md`.

## Guías individuales palabra por palabra (cada uno abre SOLO la suya)
- `guias/rodrigo.md` — qué decir y teclear, código línea por línea, fallos.
- `guias/mario.md` — Gradle, Auth, CREATE 201/plan B 409.
- `guias/alberto.md` — GET O(1), Mango 2 docs, parseo, puente `_rev`.
- `guias/fernando.md` — MVCC, UPDATE, 409 forzado, DELETE, cierre literal.

## Referencia técnica
- `docs/01-rodrigo.md`, `02-mario.md`, `03-alberto.md`, `04-fernando.md` — fichas por bloque.
- `docs/05-checklist-troubleshooting.md` — checklist día antes + 10 min antes.
- `docs/06-referencia-curl.md` — chuleta curl 1 página.
- `docs/07-preguntas-jurado.md` — 14 preguntas probables con respuesta corta (leer antes de exponer).
- `docs/08-glosario.md` — términos en una línea.
- `docs/09-zed.md` — flujo con Zed como IDE (terminal, wrapper, extensión Kotlin).

## Setup por máquina (cada quien en la suya, 5 min)
```bash
# 1. Docker CouchDB (igual en todas)
docker run -d --name couchdb-server -p 5984:5984 \
  -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password couchdb:latest
curl -u admin:password http://localhost:5984/
# -> {"couchdb":"Welcome","version":"3..."}

# 2. Proyecto
git clone https://github.com/Fernando-rtx/kotlinBASEdeDATOS.git
cd kotlinBASEdeDATOS
```
**Correrlo:**
- **IntelliJ:** abrir como proyecto Gradle y botón Run en `Main.kt`.
- **Zed/VSCode/terminal:** `./gradlew run` (ver `docs/09-zed.md` si falta el wrapper jar).

Fauxton: `http://localhost:5984/_utils` — `admin/password`.

## Semilla opcional (BD con todo ya hecho)
```bash
./seed/import.sh http://localhost:5984 admin password
```
Carga `universidad` con `est_2026_01/02/03` + `alumno1..3`. Mango `Ingenieria de Sistemas` devuelve 2.
OJO expo: si importas la semilla, el CREATE de Mario dará `409` (ya existe) en vez de `201`.
Para el directo limpio, deja `est_2026_01` en `404` (ver `seed/README.md`).

## Estructura código
```
src/main/kotlin/
  Main.kt            # orquesta las 4 fases
  RodrigoModule.kt   # crearBaseDatos() PUT /universidad
  MarioModule.kt     # crearDocumento() PUT /universidad/id
  AlbertoModule.kt   # leerDocumentoPorId() GET + consultarPorCarrera() POST _find + obtenerRevision()
  FernandoModule.kt  # actualizarDocumento() PUT con _rev + eliminarDocumento() DELETE ?rev=
build.gradle.kts     # kotlin 2.2.0, okhttp:4.12.0, json:20240303, toolchain 21
docker-compose.expo.yml  # alternativa al docker run (admin/password)
seed/                # BD ejemplo importable
guias/               # guion palabra por palabra por integrante
docs/                # fichas, checklist, preguntas, glosario, Zed
```

## Reglas de la expo
- Cada uno cierra nombrando al siguiente ("...y le paso a Mario").
- Proyectan consola + Fauxton lado a lado.
- Credenciales SIEMPRE `admin/password` en expo. Casa Fernando usa `fernandojose` (ver `Main.kt`).
