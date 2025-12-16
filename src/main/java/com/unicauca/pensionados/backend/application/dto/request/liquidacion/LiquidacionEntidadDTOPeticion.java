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
public class LiquidacionEntidadDTOPeticion {
    
    @NotNull(message = "El NIT de la entidad es obligatorio")
    private Long nitEntidad;

    @NotNull(message = "El ID del periodo es obligatorio")
    private Long periodoId;

    @NotNull(message = "El total a cobrar es obligatorio")
    private BigDecimal totalACobrar;

    private BigDecimal valorCorriente = BigDecimal.ZERO;
    private BigDecimal valorNoCorriente = BigDecimal.ZERO;
    private BigDecimal valorPrescrito = BigDecimal.ZERO;
    private BigDecimal descuentos = BigDecimal.ZERO;
    private BigDecimal neto;

    private EstadoLiquidacion estado = EstadoLiquidacion.PENDIENTE;
    private LocalDate fechaLiquidacion;
    private LocalDate fechaCobro;
    private Integer pensionadosCount = 0;
    private String detalles; // JSON
}
