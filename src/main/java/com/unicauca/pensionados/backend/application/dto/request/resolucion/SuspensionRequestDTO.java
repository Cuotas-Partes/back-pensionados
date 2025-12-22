package com.unicauca.pensionados.backend.application.dto.request.resolucion;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class SuspensionRequestDTO extends ResolucionBaseRequestDTO {
    private String motivoSuspension;
    private LocalDate fechaInicioSuspension;
}
