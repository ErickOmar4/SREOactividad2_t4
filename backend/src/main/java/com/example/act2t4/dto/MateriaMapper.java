package com.example.act2t4.dto;

import com.example.act2t4.model.Materia;

/**
 * Convierte entre la entidad {@link Materia} y el {@link MateriaDTO}.
 * Centralizar la conversión evita repetirla en cada controlador.
 */
public final class MateriaMapper {

    private MateriaMapper() {
        // Clase de utilidades: no se instancia.
    }

    /** Entidad -> DTO (para enviar datos al cliente). */
    public static MateriaDTO aDto(Materia materia) {
        return new MateriaDTO(materia.id(), materia.nombre(), materia.creditos(),
                materia.semestre(), materia.area());
    }
}
