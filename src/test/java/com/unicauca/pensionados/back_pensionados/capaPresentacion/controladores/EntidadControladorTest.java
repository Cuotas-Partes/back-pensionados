// package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.MockMvc;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import org.springframework.http.MediaType;


// @SpringBootTest
// @AutoConfigureMockMvc
// class EntidadControladorTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     void registrarEntidadDebeRetornar200() throws Exception {
//         String json = """
//             {
//               "nitEntidad": 123456789,
//               "nombreEntidad": "Unicauca",
//               "direccionEntidad": "Calle 5",
//               "emailEntidad": "contacto@unicauca.edu.co",
//               "telefonoEntidad": 1234567,
//               "estadoEntidad": "ACTIVA"
//             }
//         """;

//         mockMvc.perform(post("/entidad/registrar")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(json))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Entidad registrada exitosamente"));
//     }

//     @Test
//     void buscarPorNitDebeRetornar404SiNoExiste() throws Exception {
//         mockMvc.perform(get("/entidad/buscarPorNit/99999"))
//                 .andExpect(status().isNotFound());
//     }
// }
