package com.unicauca.pensionados.backend.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.unicauca.pensionados.backend.domain.model.enums.TipoReporte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTORespuesta {
    private Long id;
    private String titulo;
    private TipoReporte tipo;
    private String filtros;
    private String datos;
    private Integer totalRegistros;
    private BigDecimal totalValor;
    private BigDecimal totalCorriente;
    private BigDecimal totalNoCorriente;
    private BigDecimal totalPrescrito;
    private LocalDateTime generadoEn;
    private String generadoPor;
    private Integer usuarioId;
}
