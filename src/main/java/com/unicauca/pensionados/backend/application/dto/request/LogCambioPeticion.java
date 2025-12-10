package com.unicauca.pensionados.backend.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogCambioPeticion {
    private Integer usuarioId;
    private String entidad;
    private String accion;
    private Object valorAnterior;
    private Object valorNuevo;
}
