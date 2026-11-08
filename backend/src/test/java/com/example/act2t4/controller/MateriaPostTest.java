package com.example.act2t4.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Pruebas de los POST (formulario @ModelAttribute y REST @RequestBody).
 * Estas pruebas AGREGAN materias, por eso el contexto se descarta al terminar la clase
 * y no afecta a las demás pruebas (que esperan 5 materias).
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MateriaPostTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void formularioValidoGuardaYRedirigeAlDetalle() throws Exception {
        mockMvc.perform(post("/materias")
                        .param("nombre", "Estructuras de Datos")
                        .param("area", "Programación")
                        .param("creditos", "5")
                        .param("semestre", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/materias/*"));
    }

    @Test
    void formularioInvalidoVuelveAlFormularioConErrores() throws Exception {
        mockMvc.perform(post("/materias")
                        .param("nombre", "")
                        .param("area", "")
                        .param("creditos", "0")
                        .param("semestre", "99"))
                .andExpect(status().isOk())
                .andExpect(view().name("materias/formulario"))
                .andExpect(model().attributeHasFieldErrors("materiaForm", "nombre", "area", "creditos", "semestre"));
    }

    @Test
    void postRestConJsonValidoDevuelve201ConElObjetoGuardado() throws Exception {
        String json = """
                {"nombre":"Cálculo Diferencial","creditos":5,"semestre":1,"area":"Ciencias"}
                """;
        mockMvc.perform(post("/api/materias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/materias/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Cálculo Diferencial"))
                .andExpect(jsonPath("$.area").value("Ciencias"));
    }

    @Test
    void postRestConJsonInvalidoDevuelve400() throws Exception {
        String json = """
                {"nombre":"","creditos":50,"semestre":1,"area":"Ciencias"}
                """;
        mockMvc.perform(post("/api/materias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
