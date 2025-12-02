package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoLiquidacion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiquidacionPensionadoDTORespuesta {
    private Long id;
    private Long pensionadoId;
    private String pensionadoCedula;
    private String pensionadoNombre;
    private Long periodoId;
    private String periodoNombre;
    private BigDecimal totalAPagar;
    private BigDecimal valorCorriente;
    private BigDecimal valorNoCorriente;
    private BigDecimal valorPrescrito;
    private BigDecimal descuentos;
    private BigDecimal netoPagable;
    private EstadoLiquidacion estado;
    private LocalDate fechaLiquidacion;
    private LocalDate fechaPago;
    private String detalles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
