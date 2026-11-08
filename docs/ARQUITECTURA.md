# Arquitectura

## Patrón MVC

| Parte | Responsabilidad | En este proyecto |
|---|---|---|
| **Modelo** | Datos y reglas del negocio | `model/Materia`, `service/MateriaService`; el DTO `dto/MateriaDTO` es su "forma externa" |
| **Vista** | Lo que ve el usuario | `templates/**/*.html` (Thymeleaf) y `frontend/index.html` |
| **Controlador** | Recibe la petición, coordina y elige la vista | `controller/MateriaViewController` (HTML), `MateriaController` y `ProyectoController` (JSON) |

## Flujo de una petición MVC (`GET /materias?area=Redes`)

1. El navegador envía la petición a Tomcat.
2. `DispatcherServlet` (Spring MVC) busca el método con `@RequestMapping("/materias")` + `@GetMapping`.
3. Spring convierte `?area=Redes` en el parámetro `@RequestParam String area`.
4. El controlador pide al servicio las materias filtradas, las convierte a DTO y las pone en el `Model`.
5. El controlador devuelve el nombre de vista `"materias/lista"`.
6. Thymeleaf procesa `templates/materias/lista.html` con los datos del `Model` (`th:each`...).
7. El HTML resultante viaja al navegador.

## Flujo del formulario (`POST /materias`)

1. El navegador envía los campos como `application/x-www-form-urlencoded`.
2. `@ModelAttribute` crea un `MateriaDTO` y copia cada campo en su setter.
3. `@Valid` evalúa las reglas; los errores quedan en `BindingResult`.
4. Con errores → se vuelve a mostrar `formulario.html` con mensajes. Sin errores → se guarda y se **redirige** a `/materias/{id}` (patrón *Post/Redirect/Get*, evita reenviar el formulario al refrescar).

## Flujo del POST REST (`POST /api/materias`)

1. Llega un JSON con `Content-Type: application/json`.
2. `@RequestBody` lo convierte en `MateriaDTO` (Jackson) y `@Valid` lo valida.
3. Se guarda y se responde `201 Created`, cabecera `Location` y el DTO (con `id`) en JSON.

## Capas

```
controller  →  service  →  model (datos)
     ↑ usa ↓
        dto (entra/sale por los controladores)
web (ControllerAdvice con @Value) · config (propiedades)
```

## ¿Por qué un DTO y no la entidad?

- **Seguridad:** el cliente no puede enviar ni ver campos internos (p. ej. el `id` lo asigna el servidor).
- **Desacople:** la entidad puede cambiar (otra base de datos) sin romper la API ni las vistas.
- **Validación:** las reglas de entrada viven en el DTO sin ensuciar la entidad.
- **Forma a medida:** el DTO tiene lo que necesita cada pantalla o endpoint.

## Frontend y backend en un solo despliegue

`frontend/` es una carpeta independiente (HTML/CSS/JS). Durante `mvn package` se copia a `static/` dentro del `.jar`, por lo que el
mismo proceso sirve el panel, las vistas y la API en un único puerto (sin CORS ni segundo servidor).
