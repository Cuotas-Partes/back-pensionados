package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

public interface IPeriodoServicio {
    PeriodoRespuesta consultarPeriodoPorAnio(int anio);    
}
