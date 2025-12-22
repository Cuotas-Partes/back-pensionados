package com.unicauca.pensionados.backend.application.dto.response.cuota;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCuotaRespuestaDTO {
    private Long id;
    private String descripcion;
    private BigDecimal valor;
}
