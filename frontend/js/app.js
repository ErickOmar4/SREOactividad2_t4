/* ==========================================================================
   Lógica del panel de inicio (frontend). JavaScript simple, sin librerías.
   Las rutas son relativas: funciona cuando el backend sirve este archivo.
   ========================================================================== */

/** Atajo para obtener un elemento por id. */
const $ = (id) => document.getElementById(id);

/** Rutas a verificar: [descripción, ruta, validador de respuesta]. */
const RUTAS = [
  ["Estado del servidor", "/api/proyecto/estado", (r) => r.ok],
  ["Valor @Value (nombre)", "/api/proyecto/nombre", (r) => r.ok],
  ["Lista JSON (DTO)", "/api/materias", (r) => r.ok],
  ["@RequestParam ?area=Redes", "/api/materias?area=Redes", (r) => r.ok],
  ["@PathVariable /api/materias/1", "/api/materias/1", (r) => r.ok],
  ["Vista lista (th:each)", "/materias", (r) => r.ok],
  ["Vista filtrada ?area=Datos", "/materias?area=Datos", (r) => r.ok],
  ["Vista detalle /materias/1", "/materias/1", (r) => r.ok],
  ["Vista formulario", "/materias/nueva", (r) => r.ok],
  ["404 controlado /materias/999", "/materias/999", (r) => r.status === 404],
];

/** Pinta una lista de resultados con punto verde (ok) o rojo (mal). */
function pintarLista(ul, filas) {
  ul.innerHTML = "";
  for (const { ok, texto, detalle } of filas) {
    const li = document.createElement("li");
    const izq = document.createElement("span");
    const punto = document.createElement("span");
    punto.className = "punto " + (ok === null ? "" : ok ? "ok" : "mal");
    izq.append(punto, texto);
    const der = document.createElement("span");
    der.className = "mono";
    der.textContent = detalle;
    li.append(izq, der);
    ul.append(li);
  }
}

/** Muestra el estado del servidor al cargar la página. */
async function cargarEstado() {
  const ul = $("estado");
  try {
    const r = await fetch("/api/proyecto/estado");
    if (!r.ok) throw new Error("HTTP " + r.status);
    const d = await r.json();
    $("marca").textContent = d.proyecto;
    document.title = d.proyecto + " · MVC con Spring Boot";
    pintarLista(ul, [
      { ok: true, texto: "Servidor " + d.estado, detalle: "puerto " + d.puerto },
      { ok: true, texto: "Proyecto", detalle: d.proyecto },
      { ok: true, texto: "Autor", detalle: d.autor },
      { ok: true, texto: "Java", detalle: d.javaVersion },
    ]);
  } catch (e) {
    pintarLista(ul, [{
      ok: false,
      texto: "No se pudo contactar al backend",
      detalle: "abre esta página desde http://localhost:8080/",
    }]);
  }
}

/** Verifica todas las rutas de RUTAS. */
async function verificar() {
  const ul = $("verificacion");
  const filas = [];
  for (const [texto, ruta, validar] of RUTAS) {
    try {
      const r = await fetch(ruta, { redirect: "follow" });
      filas.push({ ok: validar(r), texto, detalle: "HTTP " + r.status });
    } catch (e) {
      filas.push({ ok: false, texto, detalle: "sin respuesta" });
    }
    pintarLista(ul, filas);
  }
}

/** Envía el POST REST con JSON y muestra la respuesta. */
async function enviarPost(evento) {
  evento.preventDefault();
  const cuerpo = {
    nombre: $("p-nombre").value,
    area: $("p-area").value,
    creditos: Number($("p-creditos").value),
    semestre: Number($("p-semestre").value),
  };
  $("post-resultado").hidden = false;
  try {
    const r = await fetch("/api/materias", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(cuerpo),
    });
    const texto = await r.text();
    $("post-estado").textContent =
      "HTTP " + r.status + (r.headers.get("Location") ? " · Location: " + r.headers.get("Location") : "");
    try {
      $("post-json").textContent = JSON.stringify(JSON.parse(texto), null, 2);
    } catch {
      $("post-json").textContent = texto;
    }
  } catch (e) {
    $("post-estado").textContent = "Error de red";
    $("post-json").textContent = String(e);
  }
}

$("form-post").addEventListener("submit", enviarPost);
$("btn-verificar").addEventListener("click", verificar);
cargarEstado();
