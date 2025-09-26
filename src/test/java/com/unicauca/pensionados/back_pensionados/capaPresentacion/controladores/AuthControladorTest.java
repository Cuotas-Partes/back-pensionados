package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControladorTest {

    @Autowired
    private MockMvc mockMvc;


    private ResultActions response;

    @BeforeEach
    void setup() throws Exception {
        String registro = """
            {
            "username": "prueba_pensiones@unicauca.edu.co",
            "password": "pensiones",
            "nombre": "Juan",
            "apellido": "Pérez",
            "idRol": 2
            }
        """;

        response = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registro))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void registerDebeRetornar200() throws Exception {
        String jsonResponse = response.andReturn().getResponse().getContentAsString();
        String resultado;

        if (JsonPath.read(jsonResponse, "$").toString().contains("token")) {
            resultado = JsonPath.read(jsonResponse, "$.token");
        } else if (JsonPath.read(jsonResponse, "$").toString().contains("error")) {
            resultado = JsonPath.read(jsonResponse, "$.error");
        } else {
            resultado = "Respuesta inesperada: " + jsonResponse;
        }

        System.out.println(resultado);
    }

    @Test
    void loginDebeRetornar200() throws Exception {
        String json = """
            {
              "username": "prueba_pensiones@unicauca.edu.co",
              "password": "pensiones"
            }
        """;

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        String token = JsonPath.read(
            response.andReturn().getResponse().getContentAsString(), "$.token"
        );
        System.out.println("Token recibido: " + token);

    }
}
