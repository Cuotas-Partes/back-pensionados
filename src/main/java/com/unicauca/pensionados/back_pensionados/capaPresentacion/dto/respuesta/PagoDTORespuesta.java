package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoPago;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTORespuesta {
    
    private Long idPago;
    
    private Long nitEntidad;
    
    private String nombreEntidad;
    
    private Long idPensionado;
    
    private String nombrePensionado;
    
    private Integer anio;
    
    private BigDecimal valorPagado;
    
    private BigDecimal capital;
    
    private BigDecimal saldo; // Capital - ValorPagado
    
    private Integer ipcInicialFecha;
    
    private BigDecimal valorIpcInicial;
    
    private Integer ipcFinalFecha;
    
    private BigDecimal valorIpcFinal;
    
    private BigDecimal indexacion; // (IPC Final / IPC Inicial) * Saldo
    
    private BigDecimal saldoPendiente; // Saldo + Indexación
    
    private LocalDate fechaPago;
    
    private Boolean verificado;
    
    private TipoPago tipoPago;
    
    private String observaciones;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}

