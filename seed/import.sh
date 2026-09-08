#!/bin/bash
# Importa la BD de ejemplo `universidad` en cualquier máquina.
# Uso: ./import.sh [http://localhost:5984] [admin] [password]
set -u
COUCH="${1:-http://localhost:5984}"
USER="${2:-admin}"
PASS="${3:-admin}"
AUTH="$USER:$PASS"
DIR="$(cd "$(dirname "$0")" && pwd)"

echo "== CouchDB: $COUCH (user $USER) =="
curl -s -m 5 "$COUCH/" || { echo "ERROR: CouchDB no responde en $COUCH. Levántalo primero."; exit 1; }
echo ""
echo "== Crear BD universidad (201 nueva, 412 ya existía) =="
curl -s -u "$AUTH" -X PUT "$COUCH/universidad" -w "\nHTTP:%{http_code}\n"
echo "== Importar docs =="
for f in "$DIR"/docs/*.json; do
  id="$(basename "$f" .json)"
  code=$(curl -s -o /dev/null -w "%{http_code}" -u "$AUTH" -X PUT "$COUCH/universidad/$id" -H "Content-Type: application/json" -d @"$f")
  echo "PUT $id -> HTTP $code"
done
echo "== Verificar =="
curl -s -u "$AUTH" "$COUCH/universidad/_all_docs" | python3 -c "import json,sys; d=json.load(sys.stdin); print('total:', d['total_rows'], sorted(r['id'] for r in d['rows']))"
