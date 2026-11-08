package com.example.act2t4.model;

/**
 * Entidad (modelo de datos) de una materia. Representa cómo se guarda la información
 * dentro de la aplicación; <strong>no</strong> se expone directamente en las vistas ni en
 * las peticiones: para eso existe {@link com.example.act2t4.dto.MateriaDTO}.
 *
 * @param id       identificador único
 * @param nombre   nombre de la materia
 * @param creditos créditos de la materia
 * @param semestre semestre en el que se cursa
 * @param area     categoría de la materia (Programación, Datos, Redes, Sistemas...)
 */
public record Materia(int id, String nombre, int creditos, int semestre, String area) {
}
