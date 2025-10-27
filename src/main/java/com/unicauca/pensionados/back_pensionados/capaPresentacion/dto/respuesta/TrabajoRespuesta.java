package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

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
        private Long numeroIdPersona;
        private String entidadJubilacion;
        private Long diasDeServicio;
}
    