package com.unicauca.pensionados.backend.application.dto.request.resolucion;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReactivacionRequestDTO extends  ResolucionBaseRequestDTO{
    private Long resolucionSuspensionId;
    private LocalDate fechaReactivacion;
}
