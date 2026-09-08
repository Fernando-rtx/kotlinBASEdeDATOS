#!/bin/bash
# Re-exporta la BD actual a docs/*.json + universidad_seed.json
# Uso: ./export.sh [http://localhost:5984] [admin] [pass]
set -u
COUCH="${1:-http://localhost:5984}"
USER="${2:-admin}"
PASS="${3:-admin}"
AUTH="$USER:$PASS"
DIR="$(cd "$(dirname "$0")" && pwd)"
mkdir -p "$DIR/docs"
curl -s -u "$AUTH" "$COUCH/universidad/_all_docs?include_docs=true" | python3 -c "
import json,sys,pathlib
d = json.load(sys.stdin)
seed = {}
base = pathlib.Path('$DIR')
for r in d['rows']:
    doc = dict(r['doc'])
    doc.pop('_rev', None)
    _id = doc.pop('_id')
    seed[_id] = doc
    (base/'docs'/(_id+'.json')).write_text(json.dumps(doc, indent=2, ensure_ascii=False)+'\n')
(base/'universidad_seed.json').write_text(json.dumps(seed, indent=2, ensure_ascii=False)+'\n')
print('exportados:', sorted(seed.keys()))
"
