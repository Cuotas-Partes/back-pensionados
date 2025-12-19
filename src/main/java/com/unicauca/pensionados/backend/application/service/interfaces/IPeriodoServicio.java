package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.request.periodo.RegistroPeriodoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.PeriodoRespuesta;

import java.util.List;

public interface IPeriodoServicio {
    PeriodoRespuesta crear(RegistroPeriodoPeticion peticion);
    PeriodoRespuesta actualizar(Long id, RegistroPeriodoPeticion peticion);
    void eliminar(Long id);

    PeriodoRespuesta obtenerPorId(Long id);
    List<PeriodoRespuesta> listar();
}
