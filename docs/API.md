# Referencia de rutas y pruebas con Postman / Bruno

`{base}` = `http://localhost:8080` en local o `http://IP_VPS:8083` en el servidor.

## POST /api/materias  (Video 16)

- **Headers:** `Content-Type: application/json`
- **Body (raw → JSON):**
  ```json
  { "nombre": "Estructuras de Datos", "creditos": 5, "semestre": 2, "area": "Programación" }
  ```
- **Respuesta `201 Created`:**
  ```json
  { "id": 6, "nombre": "Estructuras de Datos", "creditos": 5, "semestre": 2, "area": "Programación" }
  ```
  Cabecera: `Location: {base}/api/materias/6`.
- **Reglas del DTO:** `nombre` y `area` no vacíos; `creditos` 1-10; `semestre` 1-12. Si fallan → `400 Bad Request`.

### Paso a paso en Postman
1. **Import** → selecciona `postman/INICIALESact2_t4.postman_collection.json`.
2. Edita las variables de la colección: `base_url`.
3. Abre *POST /api/materias (válido → 201)* → **Send**.
4. Captura la pantalla mostrando método, URL, body y la respuesta `201` (guárdala como `docs/capturas/05-post-postman.png`).

### Paso a paso en Bruno
1. *New Request* → método `POST` → URL `{base}/api/materias`.
2. Pestaña **Body** → JSON → pega el cuerpo. Pestaña **Headers** → `Content-Type: application/json`.
3. **Send** y captura.

## GET

| Ruta | Parámetros | Notas |
|---|---|---|
| `/api/materias` | `nombre`, `creditos`, `semestre`, `area` (opcionales) | `nombre` y `area` ignoran mayúsculas y acentos |
| `/api/materias/{id}` | `id` entero | `404` con mensaje si no existe |
| `/api/proyecto/nombre` | — | `text/plain` |
| `/api/proyecto/estado` | — | JSON con estado, proyecto, autor, versión de Java y puerto |

## Vistas HTML

`/materias`, `/materias?area=Redes`, `/materias/{id}`, `/materias/nueva` (ver README, sección 5).
