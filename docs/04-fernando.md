# Fernando — MVCC, UPDATE, 409 y DELETE (4 min)

## Objetivo
Demostrar: sin bloqueos, con revisiones obligatorias y borrado lógico.

## Guion
1. (1.5 min) "CouchDB nunca sobrescribe a ciegas ni bloquea tablas. Cada doc tiene cadena de revisiones. Para cambiarlo DEBES mandar el `_rev` vigente. Rev vieja/nula → 409."
2. (1.5 min) `actualizarDocumento`: PUT con `_rev` → nace `2-...`. Forzar 409 con rev vieja.
3. (1 min) `DELETE ?rev=` → 200 (tombstone `_deleted:true`). Rodrigo refresca Fauxton: 404.

## Curls equivalentes
```bash
U=admin:password; C=http://localhost:5984
REV1=$(curl -s -u $U $C/universidad/est_2026_01 | python3 -c "import json,sys; print(json.load(sys.stdin)['_rev'])")
curl -s -u $U -X PUT $C/universidad/est_2026_01 -H "Content-Type: application/json" \
  -d "{\"_rev\":\"$REV1\",\"nombre\":\"Juan Perez Actualizado\",\"carrera\":\"Ingenieria de Sistemas\",\"edad\":23}"
REV2=$(curl -s -u $U $C/universidad/est_2026_01 | python3 -c "import json,sys; print(json.load(sys.stdin)['_rev'])")
curl -s -u $U -X PUT $C/universidad/est_2026_01 -H "Content-Type: application/json" \
  -d "{\"_rev\":\"$REV1\",\"nombre\":\"Obsoleto\",\"carrera\":\"Ingenieria de Sistemas\",\"edad\":99}" -w "\nHTTP:%{http_code}\n"
# -> 409 conflict
curl -s -u $U -X DELETE "$C/universidad/est_2026_01?rev=$REV2" -w "\nHTTP:%{http_code}\n"
# -> 200 {"ok":true}
```

## Salida esperada app
```
[Fernando - UPDATE] Documento actualizado correctamente.
 -> Codigo HTTP: 201
 -> Nueva revision generada: 2-...
[Fernando - DELETE] Documento eliminado satisfactoriamente.
 -> Codigo HTTP: 200
```

## Cierre
"Termina el CRUD sin bloqueos y con historial. Rodrigo refresca Fauxton: 404 deleted."
