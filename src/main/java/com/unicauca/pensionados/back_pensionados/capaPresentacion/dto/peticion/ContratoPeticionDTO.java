package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContratoPeticionDTO {
    private Long pensionadoId;   // FK a PENSIONADO
    private Long entidadNit;     // FK a ENTIDAD
    private Long personaId;      // FK a PERSONA
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String cargo;
    private String tipoVinculacion;       // "PLANTA","CATEDRATICO","OCASIONAL","OPS"
    private BigDecimal salarioBase;
    private BigDecimal ultimoSalarioReportado;
    private Integer tiempoServicioDias;
    private String estado;                 // "ACTIVO","FINALIZADO","SUSPENDIDO"

}
