# Preguntas probables del jurado (con respuesta corta)

1. **¿Por qué CouchDB y no MySQL?** — Dato documental JSON sin esquema fijo + API HTTP nativa: sin driver JDBC, cualquier lenguaje con HTTP opera la base.
2. **¿Qué es MVCC?** — Control de concurrencia multiversión: cada escritura crea una revisión nueva; el cliente debe mandar el `_rev` vigente; rev vieja → `409`. Evita escrituras perdidas sin bloquear lecturas/escrituras.
3. **¿409 vs 412?** — `409 conflict`: el documento existe o la rev está obsoleta. `412 precondition failed`: la base ya existía al hacer `PUT /db`.
4. **¿Qué es el tombstone?** — Borrado lógico: CouchDB guarda un marcador `_deleted:true` con rev nueva (`3-...`); el doc desaparece de consultas pero queda historial para replicación.
5. **¿Mango vs MapReduce?** — Mango (`POST /db/_find`) es declarativo estilo MongoDB con `selector`; MapReduce exige funciones JS en vistas. Usamos Mango por simplicidad.
6. **¿Por qué O(1) el GET por ID?** — Acceso directo por clave primaria en el B-Tree, sin escanear.
7. **¿Por qué OkHttp?** — Cliente HTTP maduro de la JVM; la app Kotlin solo necesita HTTP+JSON, no driver nativo.
8. **¿Qué hace `Credentials.basic`?** — Genera `Authorization: Basic base64(user:pass)` que CouchDB valida en cada request; sin ella → `401`.
9. **¿Qué es Fauxton?** — Consola web de CouchDB (`/_utils`) para ver bases, docs y revisiones.
10. **¿Para qué Docker?** — Entorno idéntico por máquina: misma imagen `couchdb:3`, mismo puerto 5984 y credenciales; `docker run` levanta todo.
11. **¿Qué pasa si dos actualizan a la vez?** — El primero gana (nueva rev); el segundo recibe `409` y debe re-leer y reintentar.
12. **¿Por qué `fields` en Mango?** — Proyección: trae solo lo pedido, menos red; sin `fields` trae docs completos.
13. **¿`_id` vs `_rev`?** — `_id` identifica el documento (lo elegimos: `est_2026_01`); `_rev` identifica la versión (la asigna CouchDB: `1-...`, `2-...`).
14. **¿Basic Auth es seguro?** — Solo para expo local; en producción se usaría HTTPS + usuarios/roles de CouchDB.
