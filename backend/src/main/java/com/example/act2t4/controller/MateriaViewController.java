package com.example.act2t4.controller;

import com.example.act2t4.dto.MateriaDTO;
import com.example.act2t4.dto.MateriaMapper;
import com.example.act2t4.model.Materia;
import com.example.act2t4.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador MVC (el "C" de Modelo-Vista-Controlador): recibe peticiones, prepara datos en el
 * {@link Model} y devuelve el <em>nombre de una vista</em> Thymeleaf (no JSON).
 *
 * <ul>
 *   <li>Video 8  — {@code @RequestMapping} a nivel de clase: todas las rutas empiezan en /materias.</li>
 *   <li>Video 9  — objeto {@link Model}: lleva datos del controlador a la vista.</li>
 *   <li>Video 13 — {@code @ModelAttribute}: enlaza el formulario con el DTO.</li>
 *   <li>Video 14 — {@code @RequestParam}: filtro por área (?area=Redes).</li>
 *   <li>Video 15 — {@code @PathVariable}: detalle por id (/materias/3).</li>
 * </ul>
 */
@Controller
@RequestMapping("/materias")
public class MateriaViewController {

    private final MateriaService servicio;

    public MateriaViewController(MateriaService servicio) {
        this.servicio = servicio;
    }

    /** GET /materias  o  GET /materias?area=Redes  -> templates/materias/lista.html */
    @GetMapping
    public String listar(@RequestParam(name = "area", required = false) String area, Model model) {
        List<MateriaDTO> materias = servicio.filtrar(null, null, null, area).stream()
                .map(MateriaMapper::aDto)
                .toList();
        model.addAttribute("materias", materias);
        model.addAttribute("areas", servicio.areas());
        model.addAttribute("areaSeleccionada", area == null ? "" : area.trim());
        return "materias/lista";
    }

    /** GET /materias/{id} -> templates/materias/detalle.html (404 si no existe). */
    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") int id, Model model) {
        MateriaDTO materia = servicio.buscarPorId(id)
                .map(MateriaMapper::aDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la materia con id " + id));
        model.addAttribute("materia", materia);
        return "materias/detalle";
    }

    /** GET /materias/nueva -> formulario vacío. */
    @GetMapping("/nueva")
    public String formulario(Model model) {
        model.addAttribute("materiaForm", new MateriaDTO());
        model.addAttribute("areas", servicio.areas());
        return "materias/formulario";
    }

    /**
     * POST /materias — Spring crea un MateriaDTO y le copia los campos del formulario
     * (@ModelAttribute). Si hay errores de validación se vuelve a mostrar el formulario;
     * si todo está bien se guarda y se redirige al detalle (patrón Post/Redirect/Get).
     */
    @PostMapping
    public String guardar(@Valid @ModelAttribute("materiaForm") MateriaDTO materiaForm,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes redirect) {
        if (resultado.hasErrors()) {
            model.addAttribute("areas", servicio.areas());
            return "materias/formulario";
        }
        Materia creada = servicio.crear(materiaForm);
        redirect.addFlashAttribute("mensaje",
                "Materia \"" + creada.nombre() + "\" guardada con id " + creada.id());
        return "redirect:/materias/" + creada.id();
    }
}
