package com.unicauca.pensionados.backend.application.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogCambioRespuesta {
    private Long id;
    private String entidad;
    private String accion;
    private String valorAnterior;
    private String valorNuevo;
    private LocalDateTime fecha;
    private Integer usuarioId;

}
