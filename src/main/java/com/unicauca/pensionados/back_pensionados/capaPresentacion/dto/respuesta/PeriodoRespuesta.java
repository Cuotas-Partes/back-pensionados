package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoRespuesta {
    private int anio;
    private Date fechaInicioPeriodo;
    private Date fechaFinPeriodo;
    private Double ipc;
}
