# INICIALESact2_t4 · MVC con Spring Boot y Thymeleaf

> Proyecto  **Actividad 2 – Tema 4**. Una aplicación **Spring Boot** patrón
> **Modelo-Vista-Controlador (MVC)** con vistas **Thymeleaf**, clases **DTO**, parámetros (`@RequestParam`,
> `@PathVariable`, `@ModelAttribute`), valores externos con `@Value` y un endpoint **POST REST** probado con Postman.


## Tabla de contenido

1. [Tecnologías utilizadas](#1-tecnologías-utilizadas)
2. [Arquitectura](#2-arquitectura)
3. [Requisitos para ejecutar el proyecto](#3-requisitos-para-ejecutar-el-proyecto)
4. [Cómo ejecutar el proyecto](#4-cómo-ejecutar-el-proyecto)
5. [Rutas disponibles](#5-rutas-disponibles)
6. [Evidencias (capturas)](#6-evidencias-capturas)
7. [Estructura del proyecto](#7-estructura-del-proyecto)
8. [Pruebas automatizadas](#8-pruebas-automatizadas)
9. [Despliegue en el VPS](#9-despliegue-en-el-vps)
10. [Solución de problemas](#10-solución-de-problemas)
11. [Documentación adicional](#11-documentación-adicional)
12. [Estándares y convenciones](#12-estándares-y-convenciones)

---

## 1. Tecnologías utilizadas

Cada una se explica con tres preguntas: **qué es**, **para qué sirve** y **cómo se usó aquí**.
El detalle ampliado (con fragmentos de configuración) está en [`docs/TECNOLOGIAS.md`](docs/TECNOLOGIAS.md).

| Tecnología | Versión | Qué es | Cómo se usa en el proyecto |
|---|---|---|---|
| **Java (JDK)** | 17 (compila con 17; funciona con 21) | Lenguaje y plataforma de ejecución | `records` para modelo y propiedades; clases para el DTO |
| **Maven** | 3.9+ | Gestor de dependencias y construcción | `backend/pom.xml`; genera `target/act2-t4.jar` |
| **Spring Boot** | 3.5.7 | Framework que autoconfigura la app y trae servidor embebido | `@SpringBootApplication` en `Act2T4Application` |
| **Spring Web (MVC)** | incluido en Boot | Controladores, rutas y parámetros HTTP | `@Controller`, `@RestController`, `@RequestMapping`... |
| **Thymeleaf** | incluido en Boot | Motor de plantillas HTML del lado del servidor | `templates/` con `th:each`, `th:object`, `th:field`, fragmentos |
| **Bean Validation** | incluido en Boot | Reglas declarativas (`@NotBlank`, `@Min`...) | Validación de `MateriaDTO` en formulario y API |
| **Jackson** | incluido en Boot | Conversión objeto ⇄ JSON | Respuesta y cuerpo de `/api/materias` |
| **Tomcat embebido** | incluido en Boot | Servidor web dentro del `.jar` | Escucha en `server.port` |
| **HTML + CSS + JS** | estándar | Frontend sin librerías | `frontend/index.html` (panel de acceso y probador POST) |
| **JUnit 5 + MockMvc** | incluido en Boot | Pruebas automatizadas | `backend/src/test/` |
| **Postman / Bruno** | cualquiera reciente | Cliente para probar peticiones HTTP | Colección en `postman/` |
| **Git + GitHub** | Git 2.30+ | Control de versiones | Repositorio independiente de la Actividad 1 |

### videos de la actividad

| Video | Tema | Dónde verlo en el código |
|---|---|---|
| 8 | Request Mapping | `@RequestMapping("/materias")` y `("/api/materias")` en los controladores |
| 9 | Objeto Model | `MateriaViewController.listar(...)` → `model.addAttribute("materias", ...)` |
| 10 | MVC | Modelo: `model/` · Vista: `templates/` · Controlador: `controller/` |
| 11 | Clases DTO | `dto/MateriaDTO.java` y `dto/MateriaMapper.java` |
| 12 | Directivas de Thymeleaf | `th:each`, `th:if`, `th:text`, `th:href`, `th:object`, `th:field`, `th:replace` |
| 13 | `@ModelAttribute` | `MateriaViewController.guardar(...)` y `formulario.html` |
| 14 | `@RequestParam` | `GET /materias?area=Redes` y `GET /api/materias?area=...` |
| 15 | `@PathVariable` | `GET /materias/{id}` y `GET /api/materias/{id}` |
| 16 | POST con Postman | `POST /api/materias` + colección en `postman/` |
| 17 | `@Value` | `web/VistaGlobalAdvice.java` → nombre, autor y lema en las vistas |

---

## 2. Arquitectura

```
 Navegador / Postman
        │  HTTP
        ▼
┌───────────────────────────── Spring Boot (un solo .jar, un solo puerto) ─────────────────────────────┐
│                                                                                                       │
│  Controlador MVC ──► Servicio ──► Modelo (Materia)          Vista Thymeleaf (templates/*.html)        │
│  MateriaViewController   MateriaService   datos en memoria        ▲                                   │
│       │  Model + nombre de vista ───────────────────────────────┘                                    │
│       │                                                                                               │
│  Controlador REST ──► DTO (MateriaDTO) ──► JSON (Jackson)                                             │
│  MateriaController   ProyectoController                                                               │
│                                                                                                       │
│  Recursos estáticos: index.html, css/, js/  (provienen de la carpeta frontend/)                       │
└───────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

- **Modelo:** `Materia` (entidad) y `MateriaService` (datos y reglas). El `MateriaDTO` es el contrato con el exterior.
- **Vista:** plantillas Thymeleaf en `backend/src/main/resources/templates/` (HTML generado en el servidor).
- **Controlador:** `MateriaViewController` (devuelve nombres de vista) y `MateriaController` (devuelve JSON).
- **Frontend:** `frontend/` contiene el `index.html` de acceso. Maven lo copia dentro del `.jar`
  (`pom.xml` → `<resources>`), así se sirve en `/` sin otro servidor.

Más detalle y diagramas de flujo: [`docs/ARQUITECTURA.md`](docs/ARQUITECTURA.md).

---

## 3. Requisitos para ejecutar el proyecto

| Requisito | Versión mínima | Cómo obtenerlo | Verificar |
|---|---|---|---|
| JDK | 17 | [Adoptium Temurin](https://adoptium.net/) o `sudo apt install openjdk-17-jdk` | `java -version` |
| Maven | 3.9 | [maven.apache.org/download](https://maven.apache.org/download.cgi) o `sudo apt install maven` | `mvn -v` |
| Git | 2.30 | [git-scm.com](https://git-scm.com/downloads) | `git --version` |
| Navegador | cualquiera moderno | — | — |
| Postman o Bruno (opcional) | reciente | [postman.com/downloads](https://www.postman.com/downloads/) · [usebruno.com](https://www.usebruno.com/) | — |

Conexión a internet la primera vez (Maven descarga las dependencias). Puerto libre: 8080 en local.

---

## 4. Cómo ejecutar el proyecto

```bash```
# 1. Clonar el repositorio

```
git clone https://github.com/ErickOmar4/SREOactividad2_t4.git
```
dirigirse a la capeta del backend 
```
cd backend
```

# 2. Ejecutar las pruebas (opcional pero recomendado)
``` mvn test
```
# 3. Arrancar la aplicación
```
mvn spring-boot:run
```

Abre **<http://localhost:9230/>** → verás el panel `index.html` con accesos a todo.

Otro puerto o autor sin tocar el código:

```bash
SERVER_PORT=8083 PROYECTO_AUTOR="Apellidos Nombre" mvn spring-boot:run
```

Generar y ejecutar el `.jar`:

```bash
mvn clean package
java -jar target/act2-t4.jar --server.port=9230
```

---

## 5. Rutas disponibles

### Vistas (HTML · Thymeleaf)

| Ruta | Anotación clave | Descripción |
|---|---|---|
| `GET /` | — | Panel `index.html` (frontend) |
| `GET /materias` | `Model`, `th:each` | Lista de materias |
| `GET /materias?area=Redes` | `@RequestParam` | Lista filtrada por área |
| `GET /materias/{id}` | `@PathVariable` | Detalle (404 si no existe) |
| `GET /materias/nueva` | `Model` | Formulario vacío |
| `POST /materias` | `@ModelAttribute` | Procesa el formulario → redirige al detalle |

### API REST (JSON)

| Ruta | Descripción |
|---|---|
| `GET /api/materias` (`?nombre=&creditos=&semestre=&area=`) | Lista de `MateriaDTO` |
| `GET /api/materias/{id}` | Una materia (404 si no existe) |
| `POST /api/materias` | Crea una materia desde un JSON (`@RequestBody`) → `201 Created` |
| `GET /api/proyecto/nombre` | Texto con `proyecto.nombre` |
| `GET /api/proyecto/estado` | Estado del servidor (JSON) |

Ejemplo del POST (también en [`postman/`](postman/INICIALESact2_t4.postman_collection.json)):

```bash
curl -i -X POST http://localhost:9230/api/materias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Estructuras de Datos","creditos":5,"semestre":2,"area":"Programación"}'
```

Respuesta esperada: `HTTP/1.1 201` con cabecera `Location: .../api/materias/6` y el JSON con `"id": 6`.
Guía completa: [`docs/API.md`](docs/API.md).

-
## 7. Estructura del proyecto

```
INICIALESact2_t4/
├── backend/                         ← aplicación Spring Boot
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/example/act2t4/
│       │   ├── Act2T4Application.java
│       │   ├── config/ProyectoProperties.java
│       │   ├── controller/  MateriaViewController · MateriaController · ProyectoController
│       │   ├── dto/         MateriaDTO · MateriaMapper
│       │   ├── model/       Materia · EstadoResponse
│       │   ├── service/     MateriaService
│       │   └── web/         VistaGlobalAdvice
│       ├── main/resources/
│       │   ├── application.properties
│       │   └── templates/   fragments/ · materias/ · error/
│       └── test/java/...    pruebas con MockMvc
├── frontend/                        ← panel de acceso (se copia al .jar)
│   ├── index.html
│   ├── css/styles.css
│   └── js/app.js
├── docs/                            ← documentación ampliada y capturas
├── postman/                         ← colección de peticiones
├── scripts/vps-run.sh               ← iniciar/detener con nohup
├── CHANGELOG.md · .gitignore · .gitattributes · .editorconfig
└── README.md
```

---

## 8. Pruebas automatizadas

```bash
cd backend
mvn test
```

Incluyen: vistas (modelo, filtro, detalle, 404, `@Value`), POST de formulario (válido e inválido),
POST REST (201 y 400), filtros de la API y el estado del proyecto.

---

## 9. Despliegue en el VPS

Resumen (detalle en [`docs/DESPLIEGUE_VPS.md`](docs/DESPLIEGUE_VPS.md)):

```bash
# En tu máquina
cd backend && mvn clean package
scp target/act2-t4.jar ../scripts/vps-run.sh usuario@IP_VPS:~/act2_t4/

# En el VPS (carpeta NUEVA; la Actividad 1 no se toca)
cd ~/act2_t4 && chmod +x vps-run.sh
PORT=8083 ./vps-run.sh start
curl http://localhost:8083/api/proyecto/estado
```

La Actividad 1 sigue en su puerto original (por ejemplo 8081) y esta en el **8083**.
Recuerda abrir el puerto en el firewall del VPS (`sudo ufw allow 8083/tcp`).

---
