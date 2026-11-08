package com.example.act2t4.controller;

import com.example.act2t4.config.ProyectoProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/** Pruebas de las VISTAS Thymeleaf (solo lectura: no modifica los datos). */
@SpringBootTest
@AutoConfigureMockMvc
class MateriaViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProyectoProperties propiedades;

    @Test
    void listaUsaLaVistaYElModeloConTodasLasMaterias() throws Exception {
        mockMvc.perform(get("/materias"))
                .andExpect(status().isOk())
                .andExpect(view().name("materias/lista"))
                .andExpect(model().attribute("materias", hasSize(5)))
                .andExpect(content().string(containsString("Bases de Datos")));
    }

    @Test
    void requestParamFiltraPorArea() throws Exception {
        mockMvc.perform(get("/materias").param("area", "redes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("materias", hasSize(1)))
                .andExpect(content().string(containsString("Redes de Computadoras")));
    }

    @Test
    void pathVariableMuestraElDetalle() throws Exception {
        mockMvc.perform(get("/materias/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("materias/detalle"))
                .andExpect(model().attributeExists("materia"))
                .andExpect(content().string(containsString("Bases de Datos")));
    }

    @Test
    void detalleDeUnIdInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/materias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void formularioSeMuestraConUnDtoVacio() throws Exception {
        mockMvc.perform(get("/materias/nueva"))
                .andExpect(status().isOk())
                .andExpect(view().name("materias/formulario"))
                .andExpect(model().attributeExists("materiaForm"));
    }

    @Test
    void laVistaMuestraElValorLeidoConValue() throws Exception {
        mockMvc.perform(get("/materias"))
                .andExpect(content().string(containsString(propiedades.nombre())))
                .andExpect(content().string(containsString("Aprendiendo Spring Boot paso a paso")));
    }
}
