package com.unicauca.pensionados.backend.application.dto.request.liquidacion;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HistoricoLiquidacionPeticionDTO {

    // IDs de relaciones
    private Long idPensionado;
    private Long idEntidad;

    // Periodo liquidado
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
}

