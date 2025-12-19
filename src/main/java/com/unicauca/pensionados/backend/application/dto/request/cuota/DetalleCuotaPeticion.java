package com.unicauca.pensionados.backend.application.dto.request.cuota;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCuotaPeticion {
    private String descripcion;
    private BigDecimal valor;
}
