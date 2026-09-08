# Rodrigo — Infra, Docker y fundamentos (3 min)

## Objetivo
Dejar CouchDB corriendo y justificar NoSQL documental + HTTP, y crear la BD `universidad`.

## Guion sugerido
1. (1.5 min teoría) "CouchDB es NoSQL orientado a documentos JSON, cada uno con `_id` único. Diferencia clave: no usa socket binario ni driver JDBC; expone API REST con JSON. Cualquier cosa que hable HTTP — curl, navegador, Kotlin — opera la base."
2. (1.5 min demo) Levantar contenedor, probar salud, abrir Fauxton, crear BD.

## Comandos exactos (proyectados, en orden)
```bash
docker run -d --name couchdb-server -p 5984:5984 \
  -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password couchdb:latest
docker ps
curl -u admin:password http://localhost:5984/
# -> {"couchdb":"Welcome","version":"3.5.2",...}
curl -u admin:password -X PUT http://localhost:5984/universidad -w "\nHTTP:%{http_code}\n"
# 201 creada / 412 ya existía
curl -s -u admin:password http://localhost:5984/_all_dbs
```

## Fauxton
- `http://localhost:5984/_utils`, login `admin/password`.
- Mostrar catálogo, entrar a `universidad`. Refrescar al final cuando Fernando borre.

## Código (señalar)
`RodrigoModule.crearBaseDatos()`: `PUT $COUCHDB_URL/$DB_NAME` con body vacío y header `Authorization`. `when (response.code)`: 201 creada, 412 ya existía.

## Fallos típicos
- `curl: (7) Failed to connect` → `docker ps`, `docker logs couchdb-server`.
- `401` → mal user/pass (expo: `password`).
- Puerto ocupado → otro servicio en 5984.

## Transición
"...con la base lista, le paso a **Mario**, que conecta Kotlin y crea el primer documento."
