package com.example.act2t4.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) de {@link com.example.act2t4.model.Materia}.
 *
 * <p>Es el objeto que viaja entre el cliente y el servidor: lo usan las vistas Thymeleaf
 * (formulario con {@code @ModelAttribute}) y el endpoint REST ({@code @RequestBody}).
 * Se escribe como clase con getters y setters (no como record) porque Thymeleaf
 * ({@code th:field}) y el enlace de formularios necesitan métodos {@code getX()/setX()}.</p>
 *
 * <p>Incluye reglas de validación (Bean Validation) para rechazar datos incorrectos
 * antes de que lleguen a la lógica de negocio.</p>
 */
public class MateriaDTO {

    /** Lo asigna el servidor; el cliente no lo envía al crear. */
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre admite máximo 80 caracteres")
    private String nombre;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Mínimo 1 crédito")
    @Max(value = 10, message = "Máximo 10 créditos")
    private Integer creditos;

    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 12, message = "El semestre máximo es 12")
    private Integer semestre;

    @NotBlank(message = "El área es obligatoria")
    @Size(max = 40, message = "El área admite máximo 40 caracteres")
    private String area;

    /** Constructor vacío: requerido por Spring/Jackson para crear el objeto y llenarlo con setters. */
    public MateriaDTO() {
    }

    public MateriaDTO(Integer id, String nombre, Integer creditos, Integer semestre, String area) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestre = semestre;
        this.area = area;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
