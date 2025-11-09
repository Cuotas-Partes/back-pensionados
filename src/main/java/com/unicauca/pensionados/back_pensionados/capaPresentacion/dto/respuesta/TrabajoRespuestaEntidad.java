package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrabajoRespuestaEntidad {
        private Long nitEntidad;
        private Long idPersona; // <-- CAMBIO: Renombrado de numeroIdPersona a idPersona
        private Long diasDeServicio;
}
    