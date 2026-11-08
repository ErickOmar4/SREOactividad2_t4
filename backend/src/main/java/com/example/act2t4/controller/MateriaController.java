package com.example.act2t4.controller;

import com.example.act2t4.dto.MateriaDTO;
import com.example.act2t4.dto.MateriaMapper;
import com.example.act2t4.model.Materia;
import com.example.act2t4.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST (Actividad 1 + POST de la Actividad 2): responde JSON, no vistas.
 * Las vistas HTML están en {@link MateriaViewController}.
 */
@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final MateriaService servicio;

    public MateriaController(MateriaService servicio) {
        this.servicio = servicio;
    }

    /** GET /api/materias?nombre=&creditos=&semestre=&area= (todos los filtros son opcionales). */
    @GetMapping
    public List<MateriaDTO> listar(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "creditos", required = false) Integer creditos,
            @RequestParam(name = "semestre", required = false) Integer semestre,
            @RequestParam(name = "area", required = false) String area) {
        return servicio.filtrar(nombre, creditos, semestre, area).stream()
                .map(MateriaMapper::aDto)
                .toList();
    }

    /** GET /api/materias/{id} */
    @GetMapping("/{id}")
    public MateriaDTO obtener(@PathVariable("id") int id) {
        return servicio.buscarPorId(id)
                .map(MateriaMapper::aDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la materia con id " + id));
    }

    /**
     * POST /api/materias — recibe un JSON con la forma del DTO (@RequestBody),
     * lo valida (@Valid) y responde 201 Created con el objeto guardado (ya con su id).
     */
    @PostMapping
    public ResponseEntity<MateriaDTO> crear(@Valid @RequestBody MateriaDTO dto) {
        Materia creada = servicio.crear(dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.id())
                .toUri();
        return ResponseEntity.created(ubicacion).body(MateriaMapper.aDto(creada));
    }
}
