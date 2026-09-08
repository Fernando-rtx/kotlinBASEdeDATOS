# Alberto — READ por ID + Mango _find (3.5 min)

## Objetivo
Leer por clave (O(1)) y filtrar por atributo con Mango, parseando JSON en Kotlin.

## Guion
1. (1 min) "`GET /universidad/est_2026_01` busca por primaria en el B-Tree: O(1). Mostramos código + payload."
2. (2 min) "Para filtrar sin MapReduce JS usamos `POST /universidad/_find` (Mango). Selector `$eq` + `fields`."
3. (0.5 min) "El String se vuelve `JSONObject/JSONArray` manipulable."

## Mango
```json
{"selector": {"carrera": {"$eq": "Ingenieria de Sistemas"}}, "fields": ["_id", "nombre", "carrera", "edad"]}
```
```bash
curl -s -u admin:password -X POST http://localhost:5984/universidad/_find \
  -H "Content-Type: application/json" \
  -d '{"selector":{"carrera":{"$eq":"Ingenieria de Sistemas"}},"fields":["_id","nombre","carrera","edad"]}'
# con seed: est_2026_01 + est_2026_02; excluye est_2026_03 (Medicina)
```

## Salida esperada
```
[Alberto - READ] Codigo HTTP: 200
 -> Payload recibido: {"_id":"est_2026_01","_rev":"1-...",...}
[Alberto - MANGO QUERY] Codigo HTTP: 200
 -> Registros coincidentes: {"docs":[{...Juan...},{...Maria...}]}
```

## Fallos
- 404 en GET → doc no existe; recrear/importar seed.
- `docs:[]` → texto exacto `Ingenieria de Sistemas` (con "de", sin tilde).
- `warning: No matching index` → normal, igual devuelve.

## Transición
"...ya sabemos leer y filtrar, le paso a **Fernando**, que actualiza y borra con control de versiones."
