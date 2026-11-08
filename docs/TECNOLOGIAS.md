# Tecnologías: qué son, para qué sirven y cómo se configuraron

## Java 17
- **Qué es:** lenguaje y plataforma (JDK = compilador + máquina virtual + librerías).
- **Para qué:** compilar y ejecutar la aplicación.
- **Aquí:** `backend/pom.xml` fija `<java.version>17</java.version>`. Los modelos son `record`; el DTO es una clase con getters/setters.

## Maven
- **Qué es:** herramienta que descarga dependencias, compila, prueba y empaqueta.
- **Aquí:** `backend/pom.xml`. Comandos: `mvn test`, `mvn spring-boot:run`, `mvn clean package` (genera `target/act2-t4.jar`).
- **Detalle importante:** el `pom.xml` copia la carpeta `../frontend` dentro del `.jar`:
  ```xml
  <resources>
    <resource><directory>src/main/resources</directory></resource>
    <resource><directory>../frontend</directory><targetPath>static</targetPath></resource>
  </resources>
  ```
  Spring Boot sirve todo lo que esté en `static/`, y `index.html` es la página de bienvenida.

## Spring Boot
- **Qué es:** framework con autoconfiguración, *starters* y servidor embebido.
- **Aquí:** `Act2T4Application` con `@SpringBootApplication` y `@ConfigurationPropertiesScan`. Versión 3.5.7 (parent del `pom.xml`).

## Spring MVC (`spring-boot-starter-web`)
- **Para qué:** recibir peticiones HTTP y responder.
- **Anotaciones usadas**

  | Anotación | Uso en el proyecto |
  |---|---|
  | `@Controller` | Devuelve el *nombre de una vista* (`"materias/lista"`) |
  | `@RestController` | Devuelve datos (JSON) directamente |
  | `@RequestMapping("/materias")` | Prefijo común de rutas de la clase |
  | `@GetMapping` / `@PostMapping` | Método HTTP de cada ruta |
  | `@RequestParam` | Lee `?area=Redes` |
  | `@PathVariable` | Lee `{id}` de `/materias/3` |
  | `@ModelAttribute` | Llena un objeto (DTO) con los campos de un formulario |
  | `@RequestBody` | Convierte el JSON del cuerpo en un objeto (DTO) |
  | `@Valid` + `BindingResult` | Valida el DTO y recoge errores |

## Thymeleaf (`spring-boot-starter-thymeleaf`)
- **Qué es:** motor de plantillas: HTML con atributos `th:*` que el servidor rellena antes de enviarlo.
- **Configuración:** basta con la dependencia; las plantillas viven en `src/main/resources/templates/` (en `application.properties` se dejan explícitos `prefix`, `suffix` y `cache`).
- **Directivas usadas**

  | Directiva | Para qué | Dónde |
  |---|---|---|
  | `th:text` | Insertar texto (escapado) | todas las vistas |
  | `th:each` | Repetir un elemento por cada ítem | `lista.html` |
  | `th:if` | Mostrar condicionalmente | mensajes y lista vacía |
  | `th:href` / `th:action` + `@{...}` | URLs correctas (con parámetros) | enlaces y formulario |
  | `th:object` / `th:field` | Ligar formulario ↔ DTO | `formulario.html` |
  | `th:errors` / `th:errorclass` | Mostrar errores de validación | `formulario.html` |
  | `th:classappend` | Añadir clases CSS según condición | menú y chips |
  | `th:fragment` / `th:replace` | Reutilizar cabecera, menú y pie | `fragments/layout.html` |

## Bean Validation (`spring-boot-starter-validation`)
- **Para qué:** reglas declarativas en el DTO: `@NotBlank`, `@NotNull`, `@Min`, `@Max`, `@Size`.
- **Aquí:** `MateriaDTO`. En el formulario, `@Valid` + `BindingResult` devuelve el formulario con mensajes; en la API devuelve `400`.
- Para ver los errores en JSON se activó `server.error.include-binding-errors=always`.

## `@Value` y propiedades
- **Qué es:** inyecta en un campo el valor de una propiedad de `application.properties`.
- **Aquí (`web/VistaGlobalAdvice.java`):**
  ```java
  @Value("${proyecto.nombre}") private String nombreProyecto;
  @Value("${proyecto.lema:Aprendiendo Spring Boot paso a paso}") private String lema; // con valor por defecto
  ```
  Los valores se agregan a todas las vistas con `@ModelAttribute` y se muestran en el encabezado y el pie.
- Alternativa usada también (Actividad 1): `@ConfigurationProperties` → `ProyectoProperties`.
- Sobrescribir sin recompilar: variable de entorno (`PROYECTO_AUTOR=...`) o argumento (`--proyecto.lema="Hola"`).

## Jackson
Convierte objetos Java ⇄ JSON de forma automática en los `@RestController`.

## Frontend (HTML, CSS, JS)
- `frontend/index.html` es el panel de acceso; `css/styles.css` es compartido con las vistas Thymeleaf (se enlaza como `/css/styles.css`).
- `js/app.js` usa `fetch` para consultar el estado, probar rutas y enviar el POST de ejemplo.
- Paleta: blanco, verde limón (`#c8f04a`), beige (`#f5f0e6`) y gris (`#8b8f86`), definida como variables CSS.

## Postman / Bruno
Clientes HTTP para probar el POST. Importa `postman/INICIALESact2_t4.postman_collection.json` y cambia la variable `base_url`.

## Git y GitHub
Ver [`FASE0_GIT.md`](FASE0_GIT.md). El `.gitignore` excluye `target/`, archivos de IDE, logs, `.env` y claves.
