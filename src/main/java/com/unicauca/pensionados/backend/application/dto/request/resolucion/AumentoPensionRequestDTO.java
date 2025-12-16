package com.unicauca.pensionados.backend.application.dto.request.resolucion;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
@EqualsAndHashCode(callSuper = true)
@Data
public class AumentoPensionRequestDTO  extends  ResolucionBaseRequestDTO{
    private String motivoAumento; // "IPC", "MEJORA_SALARIAL", "RECONOCIMIENTO_TIEMPO"
    @NotNull
    private BigDecimal porcentajeAumento;
}
