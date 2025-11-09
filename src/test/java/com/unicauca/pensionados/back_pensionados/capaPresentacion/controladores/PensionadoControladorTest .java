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
// class PensionadoControladorTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     void listarPensionadosDebeRetornar200() throws Exception {
//         mockMvc.perform(get("/pensionado/listar"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//     }

//     @Test
//     void registrarPensionadoDebeRetornar200() throws Exception {
//         String json = """
//             {
//               "numeroIdPersona": 1001,
//               "tipoIdPersona": "CC",
//               "nombrePersona": "Carlos",
//               "apellidosPersona": "López",
//               "fechaNacimientoPersona": "1960-05-20",
//               "fechaExpedicionDocumentoIdPersona": "1978-06-01",
//               "estadoPersona": "ACTIVO",
//               "generoPersona": "M",
//               "valorInicialPension": 1200000,
//               "resolucionPension": "RES123",
//               "totalDiasTrabajo": 5000,
//               "aplicarIPCPrimerPeriodo": true,
//               "nitEntidad": 123456789
//             }
//         """;

//         mockMvc.perform(post("/pensionado/registrar")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(json))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Pensionado registrado exitosamente"));
//     }
// }

