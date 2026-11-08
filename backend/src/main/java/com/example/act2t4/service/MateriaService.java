package com.example.act2t4.service;

import com.example.act2t4.dto.MateriaDTO;
import com.example.act2t4.model.Materia;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Capa de servicio: contiene la lógica de negocio y el acceso a los datos.
 * Los datos viven en memoria (se reinician al apagar la app); en un proyecto real
 * aquí se consultaría un repositorio (base de datos).
 */
@Service
public class MateriaService {

    private final List<Materia> materias = new CopyOnWriteArrayList<>(List.of(
            new Materia(1, "Programación Orientada a Objetos", 5, 3, "Programación"),
            new Materia(2, "Bases de Datos", 5, 4, "Datos"),
            new Materia(3, "Redes de Computadoras", 4, 4, "Redes"),
            new Materia(4, "Sistemas Operativos", 5, 5, "Sistemas"),
            new Materia(5, "Ingeniería de Software", 5, 6, "Programación")
    ));

    private final AtomicInteger siguienteId = new AtomicInteger(materias.size() + 1);

    /** @return copia de todas las materias. */
    public List<Materia> listar() {
        return List.copyOf(materias);
    }

    /**
     * Filtra las materias. Cada filtro es opcional: si vale {@code null} (o vacío) no se aplica.
     * El nombre y el área se comparan sin importar mayúsculas ni acentos.
     */
    public List<Materia> filtrar(String nombre, Integer creditos, Integer semestre, String area) {
        String buscadoNombre = normalizar(nombre);
        String buscadaArea = normalizar(area);
        return materias.stream()
                .filter(m -> buscadoNombre.isEmpty() || normalizar(m.nombre()).contains(buscadoNombre))
                .filter(m -> buscadaArea.isEmpty() || normalizar(m.area()).equals(buscadaArea))
                .filter(m -> creditos == null || m.creditos() == creditos)
                .filter(m -> semestre == null || m.semestre() == semestre)
                .toList();
    }

    /**
     * @param id identificador buscado
     * @return la materia si existe, o {@link Optional#empty()} si no
     */
    public Optional<Materia> buscarPorId(int id) {
        return materias.stream()
                .filter(materia -> materia.id() == id)
                .findFirst();
    }

    /** @return las áreas distintas existentes, en orden alfabético (para los filtros de la vista). */
    public List<String> areas() {
        return materias.stream()
                .map(Materia::area)
                .distinct()
                .sorted()
                .toList();
    }

    /** Crea una materia nueva a partir del DTO recibido y le asigna un id. */
    public Materia crear(MateriaDTO dto) {
        Materia nueva = new Materia(
                siguienteId.getAndIncrement(),
                dto.getNombre().trim(),
                dto.getCreditos(),
                dto.getSemestre(),
                dto.getArea().trim());
        materias.add(nueva);
        return nueva;
    }

    private static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
    }
}
