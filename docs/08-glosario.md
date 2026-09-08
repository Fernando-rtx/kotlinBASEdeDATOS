# Glosario mínimo (una línea por término)

- **NoSQL documental:** BD que guarda documentos JSON con esquema flexible, sin tablas.
- **CouchDB:** motor NoSQL documental con API REST/JSON y replicación.
- **REST:** operar recursos con verbos HTTP (GET leer, PUT crear/actualizar, POST consultar, DELETE borrar).
- **JSON:** formato de datos de docs y respuestas (`{"ok":true,"rev":"1-..."}`).
- **Fauxton:** panel web de CouchDB en `http://localhost:5984/_utils`.
- **Docker:** contenedores con entorno idéntico; `couchdb-server` expone 5984.
- **Kotlin/JVM:** lenguaje que corre en la máquina virtual Java.
- **Gradle:** compila el proyecto y baja deps (`okhttp`, `json`).
- **OkHttp:** cliente HTTP usado para los GET/PUT/POST/DELETE.
- **Basic Auth:** `Authorization: Basic base64(user:pass)` en cada request.
- **MVCC:** concurrencia por versiones, sin bloqueos.
- **`_id`:** clave del documento (`est_2026_01`). **`_rev`:** versión (`1-...` → `2-...`).
- **409 Conflict:** revisión obsoleta o doc existente. **412:** BD ya existía. **201/200:** creado/OK. **404:** no existe (missing) o borrado (deleted).
- **Mango (`_find`):** consultas declarativas con `selector` (`$eq`) y `fields`.
- **B-Tree:** índice primario; leer por ID es O(1).
- **Tombstone:** marcador `_deleted:true` del borrado lógico.
