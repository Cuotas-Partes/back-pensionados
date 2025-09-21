package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RegistroPeticion {

    String username;
    String password;
    String email;
    String nombre;
    String apellido;
    Long idRol; // <-- AÑADIMOS ESTE CAMPO para recibir el ID del rol (ej: 1 para ADMIN, 2 para INVITADO)
}
