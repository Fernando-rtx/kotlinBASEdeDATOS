# Checklist + troubleshooting

## Día antes (cada máquina)
- [ ] `docker ps` OK. Puerto 5984 libre.
- [ ] Repo clonado + `./gradlew compileKotlin` → BUILD SUCCESSFUL.
- [ ] Una corrida `./gradlew run` punta a punta.

## 10 min antes
- [ ] Contenedor Up + `curl -u admin:password http://localhost:5984/` → Welcome.
- [ ] Fauxton logueado. Decidir modo: DIRECTO LIMPIO (404, Mario crea 201) o CON SEED (409 explicado).
- [ ] Terminal + IDE + navegador visibles.

## HTTP (memorizar)
200 GET/DELETE/_find OK. 201 PUT crea/actualiza. 401 mal auth. 404 missing (ideal pre-directo) / deleted (tras DELETE). 409 doc con rev vieja. 412 DB ya existía.

## Arreglos 30 seg
- No conecta → `docker ps`, `docker start couchdb-server` / `docker logs couchdb-server --tail 20`.
- 401 → `password` en expo (no `fernandojose`).
- `Name already in use` → `docker rm -f couchdb-server` y re-run.
- `gradlew` sin permiso → `chmod +x gradlew`. Sin jar wrapper → abrir en IntelliJ o `gradle wrapper --gradle-version 9.0.0`.
- Mango vacío → texto exacto `Ingenieria de Sistemas`.

## Transiciones
Rodrigo → Mario → Alberto → Fernando → Rodrigo refresca → fin.
