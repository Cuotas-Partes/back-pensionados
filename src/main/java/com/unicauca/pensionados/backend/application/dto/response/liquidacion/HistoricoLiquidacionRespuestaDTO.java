package com.unicauca.pensionados.backend.application.dto.response.liquidacion;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class HistoricoLiquidacionRespuestaDTO {

    private Long idLiquidacion;

    // IDs de relaciones
    private Long idPensionado;
    private Long idEntidad;

    // Periodo
    private LocalDate fechaInicioPeriodo;
    private LocalDate fechaFinPeriodo;
    private BigDecimal numeroMesadas;

    // Valores
    private BigDecimal valorPension;
    private BigDecimal valorCuotaParteMensual;
    private BigDecimal cuotaParteTotalAnio;
    private BigDecimal porcentajeCuotaParte;
    private BigDecimal porcentajeIncrementoAnual;
    private BigDecimal incrementoAdicionalLey476;
    private BigDecimal valorInicialPension;

    // Datos pensión
    private LocalDate fechaInicioPension;
    private String resolucionPension;
    private LocalDate fechaFallecimientoPensionado;

    // Sustituto
    private String nombreSustituto;
    private String documentoSustituto;

    // Otros
    private String notas;
    private String estadoLiquidacion;
    private LocalDateTime fechaRegistro;
    private String usuarioRegistro;
}

