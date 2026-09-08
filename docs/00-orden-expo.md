# Orden de la expo minuto a minuto (maestro de ceremonia: Rodrigo)

Total ~14 min. Cada uno proyecta su parte y cierra nombrando al siguiente.
Pantalla ideal: consola/IDE + Fauxton lado a lado, letra grande.

| Min | Quién | Hace | Guía |
|-----|-------|------|------|
| 0-3 | Rodrigo | Teoría NoSQL+HTTP, `docker run`, `curl` Welcome, `PUT /universidad` 201/412, Fauxton login | `guias/rodrigo.md` |
| 3-6.5 | Mario | `build.gradle.kts` 2 deps, `Credentials.basic`, `PUT est_2026_01` 201 + `rev 1-...`, verifica Fauxton | `guias/mario.md` |
| 6.5-10 | Alberto | `GET` 200 O(1), Mango `_find` `$eq` → 2 docs, parseo `JSONObject`, rescata `_rev` | `guias/alberto.md` |
| 10-14 | Fernando | MVCC, UPDATE → `2-...`, 409 forzado, `DELETE ?rev=` → 200, GET 404, Rodrigo refresca Fauxton, cierre | `guias/fernando.md` |

## Antes de empezar (5 min)
1. Contenedor Up: `docker ps` + `curl -u admin:password http://localhost:5984/`.
2. Fauxton logueado. IDE con `Main.kt`. `./gradlew run` probado una vez.
3. Decidir modo: DIRECTO LIMPIO (`est_2026_01` en 404 → Mario muestra 201) o CON SEED (existe → Mario explica 409). Ver README.

## Reglas
- Frases de pase obligatorias (están al final de cada guía).
- Si algo falla: no improvisar, ir a la sección "Si algo falla" de tu guía + `docs/05-checklist-troubleshooting.md`.
- Credenciales expo siempre `admin/password`.
