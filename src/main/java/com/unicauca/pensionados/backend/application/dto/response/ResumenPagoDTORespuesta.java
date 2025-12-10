package com.unicauca.pensionados.backend.application.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumenPagoDTORespuesta {
    
    private Long nitEntidad;
    
    private String nombreEntidad;
    
    private Integer anio;
    
    private BigDecimal totalValorPagado;
    
    private BigDecimal totalCapital;
    
    private BigDecimal totalSaldo;
    
    private BigDecimal totalIndexacion;
    
    private BigDecimal totalSaldoPendiente;
    
    private Integer cantidadPagos;
    
    private Integer cantidadPagosVerificados;
    
    private Integer cantidadPagosPendientes;
}

