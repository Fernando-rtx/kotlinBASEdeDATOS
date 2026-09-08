# Expo Kotlin + CouchDB — Guía por máquina (5 min)

Cada integrante replica esto en SU propia laptop. No se comparte máquina.
Solo se muestra cómo se hizo.

## 1. Requisitos por máquina
- Docker + Java 17+ (probado: Gradle 9 + Kotlin 2.2 + toolchain 21).
- Navegador para Fauxton.

## 2. Levantar CouchDB (2 min) — igual en todas las máquinas
```bash
docker run -d --name couchdb-server -p 5984:5984 \
  -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password \
  couchdb:latest
# o: docker compose -f docker-compose.expo.yml up -d
curl -u admin:password http://localhost:5984/
# -> {"couchdb":"Welcome","version":"3..."}
```
Fauxton: `http://localhost:5984/_utils` user `admin` / `password`.

Crear BD (lo hace Rodrigo en vivo, pero déjala lista):
```bash
curl -u admin:password -X PUT http://localhost:5984/universidad
# 201 creada, 412 ya existía
```

## 3. Proyecto Kotlin (2 min)
```bash
git clone https://github.com/Fernando-rtx/kotlinBASEdeDATOS.git
cd kotlinBASEdeDATOS
# Abrir en IntelliJ como proyecto Gradle o usar Gradle 9 + JDK 21:
./gradlew run  # si el wrapper no trae jar, genera con `gradle wrapper --gradle-version 9.0.0`
```
`build.gradle.kts` ya trae `okhttp:4.12.0` + `json:20240303`.
`Main.kt` usa `Credentials.basic("admin","password")`.

## 4. Qué muestra cada uno (solo mostrar, ya todo hecho)
- **Rodrigo:** `docker ps`, `curl`, Fauxton, `PUT /universidad` -> 201/412.
- **Mario:** `PUT /universidad/est_2026_01` -> 201 + `rev 1-...`.
- **Alberto:** `GET /universidad/est_2026_01` + `POST /universidad/_find`.
- **Fernando:** `PUT` con `_rev` -> `2-...`, reintento con rev vieja -> `409`, `DELETE ?rev=` -> `200`.

## 5. Dejar limpio antes de exponer
```bash
curl -u admin:password http://localhost:5984/universidad/est_2026_01
# si da 200, saca _rev y borra:
curl -u admin:password -X DELETE "http://localhost:5984/universidad/est_2026_01?rev=<rev>"
```

## Nota casa vs expo
- Expo estándar: `admin/password`.
- Casa Fernando (`~/servidores/couchdb`): `admin/fernandojose`. Cambia una línea en `Main.kt`.
