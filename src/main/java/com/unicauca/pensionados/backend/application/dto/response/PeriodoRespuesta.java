package com.unicauca.pensionados.backend.application.dto.response;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoPeriodo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoRespuesta {
    @Schema(description = "Año del periodo", example = "2023")
    private int anio;

    @Schema(description = "Fecha de inicio del periodo", example = "2023-01-01")
    private LocalDate fechaInicioPeriodo; // <-- Cambio de Date a LocalDate

    @Schema(description = "Fecha de fin del periodo", example = "2023-12-31")
    private LocalDate fechaFinPeriodo; // <-- Cambio de Date a LocalDate

    @Schema(description = "Valor del IPC aplicado en el periodo", example = "13.12")
    private Double ipc;

    @Schema(description = "Valor total de la cuota parte para este periodo", example = "1800000.00")
    private BigDecimal cuotaParteTotalPeriodo; // <-- Cambio de Double a BigDecimal
    
    @Schema(description = "Estado contable del periodo", example = "PENDIENTE")
    private EstadoPeriodo estadoPeriodo;
}