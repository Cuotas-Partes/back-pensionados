package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContratoRespuestaDTO {
    private Long idContrato;
    private Long pensionadoId;
    private Long entidadNit;
    private Long personaId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String cargo;
    private String tipoVinculacion;
    private BigDecimal salarioBase;
    private BigDecimal ultimoSalarioReportado;
    private Integer tiempoServicioDias;
    private String estado;
    private boolean tieneCertificado;
}
