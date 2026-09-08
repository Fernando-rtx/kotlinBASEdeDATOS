# Referencia curl (1 página)

```bash
U=admin:password; C=http://localhost:5984
curl -u $U $C/
curl -s -u $U $C/_all_dbs
curl -s -u $U -X PUT $C/universidad
curl -s -u $U -X PUT $C/universidad/est_2026_01 -H "Content-Type: application/json" -d '{"nombre":"Juan Perez","carrera":"Ingenieria de Sistemas","edad":22}'
curl -s -u $U $C/universidad/est_2026_01
curl -s -u $U -X POST $C/universidad/_find -H "Content-Type: application/json" -d '{"selector":{"carrera":{"$eq":"Ingenieria de Sistemas"}},"fields":["_id","nombre","carrera","edad"]}'
REV=$(curl -s -u $U $C/universidad/est_2026_01 | python3 -c "import json,sys; print(json.load(sys.stdin).get('_rev',''))")
[ -n "$REV" ] && curl -s -u $U -X DELETE "$C/universidad/est_2026_01?rev=$REV"
```
Casa: `U=admin:fernandojose`.
