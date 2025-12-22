package com.unicauca.pensionados.backend.application.dto.request.resolucion;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AjustePensionRequestDTO {
    private String numeroResolucion;
    private LocalDate fechaResolucion;
    private BigDecimal valorResolucion;
    private BigDecimal valorAnterior;
    private String motivoAjuste; // "CORRECCION", "ERROR_ADMINISTRATIVO", "SENTENCIA_JUDICIAL"
    private BigDecimal valorAjuste;
}
