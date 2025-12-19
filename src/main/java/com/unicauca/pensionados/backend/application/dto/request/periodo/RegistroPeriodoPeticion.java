package com.unicauca.pensionados.backend.application.dto.request.periodo;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoPeriodo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroPeriodoPeticion {
    private Integer anio;
    private LocalDate fechaInicioPeriodo;
    private LocalDate fechaFinPeriodo;
    private BigDecimal ipc;
    private BigDecimal cuotaParteTotalPeriodo;
    private EstadoPeriodo estadoPeriodo;
}
