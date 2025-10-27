package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRespuesta {
    
    String token;
}
