package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.EditarPeriodoPeticion;

public interface IPeriodoServicio {
    PeriodoRespuesta consultarPeriodoPorAnio(int anio);    
    void editarPeriodo(EditarPeriodoPeticion peticion);
}
