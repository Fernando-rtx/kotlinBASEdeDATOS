# Seed expo — BD `universidad` con todo ya hecho

Cada máquina importa esto y queda idéntica. Solo muestran cómo se hizo.

```bash
# Expo estándar (admin/password):
./seed/import.sh http://localhost:5984 admin password
# Casa Fernando (admin/fernandojose):
./seed/import.sh http://localhost:5984 admin fernandojose
```

Contenido: `est_2026_01` (Juan Perez, guía), `est_2026_02` (Maria Lopez),
`est_2026_03` (Carlos Ruiz) + `alumno1..3` previos. Mango
`carrera=Ingenieria de Sistemas` devuelve 2 docs.
