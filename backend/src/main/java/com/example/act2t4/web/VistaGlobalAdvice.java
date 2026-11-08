package com.example.act2t4.web;

import com.example.act2t4.controller.MateriaViewController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Agrega a TODAS las vistas de {@link MateriaViewController} datos comunes (nombre del
 * proyecto, autor, lema) leídos de {@code application.properties} con {@code @Value}.
 * La plantilla los muestra en el encabezado y en el pie de página.
 */
@ControllerAdvice(assignableTypes = MateriaViewController.class)
public class VistaGlobalAdvice {

    /** Video 17 — @Value inyecta el valor de una propiedad en un campo. */
    @Value("${proyecto.nombre}")
    private String nombreProyecto;

    @Value("${proyecto.autor}")
    private String autor;

    /** Con valor por defecto: si la propiedad no existe se usa el texto después de los dos puntos. */
    @Value("${proyecto.lema:Aprendiendo Spring Boot paso a paso}")
    private String lema;

    @ModelAttribute("proyectoNombre")
    public String proyectoNombre() {
        return nombreProyecto;
    }

    @ModelAttribute("proyectoAutor")
    public String proyectoAutor() {
        return autor;
    }

    @ModelAttribute("proyectoLema")
    public String proyectoLema() {
        return lema;
    }
}
