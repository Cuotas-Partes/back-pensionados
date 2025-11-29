package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Interfaces;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.ReporteDTOPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ReporteDTORespuesta;

public interface IReporteServicio {
    
    ReporteDTORespuesta crear(ReporteDTOPeticion peticion);
    
    ReporteDTORespuesta obtenerPorId(Long id);
    
    List<ReporteDTORespuesta> obtenerTodos();
    
    List<ReporteDTORespuesta> obtenerPorTipo(String tipo);
    
    List<ReporteDTORespuesta> obtenerPorUsuario(Integer usuarioId);
    
    void eliminar(Long id);
}
