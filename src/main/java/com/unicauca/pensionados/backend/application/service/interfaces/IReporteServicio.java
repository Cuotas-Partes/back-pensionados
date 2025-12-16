package com.unicauca.pensionados.backend.application.service.interfaces;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.reporte.ReporteDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.ReporteDTORespuesta;

public interface IReporteServicio {
    
    ReporteDTORespuesta crear(ReporteDTOPeticion peticion);
    
    ReporteDTORespuesta obtenerPorId(Long id);
    
    List<ReporteDTORespuesta> obtenerTodos();
    
    List<ReporteDTORespuesta> obtenerPorTipo(String tipo);
    
    List<ReporteDTORespuesta> obtenerPorUsuario(Integer usuarioId);
    
    void eliminar(Long id);
}
