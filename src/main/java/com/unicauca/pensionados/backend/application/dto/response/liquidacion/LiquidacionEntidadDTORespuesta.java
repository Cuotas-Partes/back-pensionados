package com.unicauca.pensionados.backend.application.dto.response.liquidacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoLiquidacion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiquidacionEntidadDTORespuesta {
    private Long id;
    private Long nitEntidad;
    private String entityNit;
    private String entityNombre;
    private Long periodoId;
    private String periodoNombre;
    private BigDecimal totalACobrar;
    private BigDecimal valorCorriente;
    private BigDecimal valorNoCorriente;
    private BigDecimal valorPrescrito;
    private BigDecimal descuentos;
    private BigDecimal neto;
    private EstadoLiquidacion estado;
    private LocalDate fechaLiquidacion;
    private LocalDate fechaCobro;
    private Integer pensionadosCount;
    private String detalles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
