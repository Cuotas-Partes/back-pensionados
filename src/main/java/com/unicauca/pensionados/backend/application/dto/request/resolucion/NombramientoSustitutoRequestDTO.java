package com.unicauca.pensionados.backend.application.dto.request.resolucion;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class NombramientoSustitutoRequestDTO extends ResolucionBaseRequestDTO {
    private Long sustitutoId;
    private LocalDate fechaInicioSustitucion;
}
