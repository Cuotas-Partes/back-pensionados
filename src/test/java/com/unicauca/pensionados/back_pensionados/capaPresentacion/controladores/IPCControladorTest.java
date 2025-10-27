// package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.MockMvc;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class IPCControladorTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     void listarIPC() throws Exception {
//         mockMvc.perform(get("/ipc/listar"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void buscarPorAnioNotFound() throws Exception {
//         mockMvc.perform(get("/ipc/buscarPorAnio/1990"))
//                 .andExpect(status().isNotFound());
//     }
// }
