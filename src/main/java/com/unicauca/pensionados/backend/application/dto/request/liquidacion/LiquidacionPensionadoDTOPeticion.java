package com.unicauca.pensionados.backend.application.dto.request.liquidacion;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoLiquidacion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiquidacionPensionadoDTOPeticion {
    
    @NotNull(message = "El ID del pensionado es obligatorio")
    private Long pensionadoId;

    @NotNull(message = "El ID del periodo es obligatorio")
    private Long periodoId;

    @NotNull(message = "El total a pagar es obligatorio")
    private BigDecimal totalAPagar;

    private BigDecimal valorCorriente = BigDecimal.ZERO;
    private BigDecimal valorNoCorriente = BigDecimal.ZERO;
    private BigDecimal valorPrescrito = BigDecimal.ZERO;
    private BigDecimal descuentos = BigDecimal.ZERO;
    private BigDecimal netoPagable;

    private EstadoLiquidacion estado = EstadoLiquidacion.PENDIENTE;
    private LocalDate fechaLiquidacion;
    private LocalDate fechaPago;
    private String detalles; // JSON
}
