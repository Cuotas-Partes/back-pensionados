package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Petición de login")
public class LoginPeticion {
    
    String username;
    String password;
}
