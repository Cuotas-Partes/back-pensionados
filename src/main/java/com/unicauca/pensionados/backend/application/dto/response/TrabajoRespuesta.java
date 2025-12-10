package com.unicauca.pensionados.backend.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrabajoRespuesta {
        private Long idTrabajo;
        private Long nitEntidad;
        private Long idPersona; // <-- CAMBIO: Renombrado de numeroIdPersona a idPersona
        private String entidadJubilacion;
        private Long diasDeServicio;
}