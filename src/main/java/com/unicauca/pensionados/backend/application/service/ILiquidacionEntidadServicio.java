package com.unicauca.pensionados.backend.application.service;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.LiquidacionEntidadDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.LiquidacionEntidadDTORespuesta;

public interface ILiquidacionEntidadServicio {
    
    LiquidacionEntidadDTORespuesta crear(LiquidacionEntidadDTOPeticion peticion);
    
    LiquidacionEntidadDTORespuesta actualizar(Long id, LiquidacionEntidadDTOPeticion peticion);
    
    void eliminar(Long id);
    
    LiquidacionEntidadDTORespuesta obtenerPorId(Long id);
    
    List<LiquidacionEntidadDTORespuesta> obtenerTodas();
    
    List<LiquidacionEntidadDTORespuesta> obtenerPorEntidad(Long nitEntidad);
    
    List<LiquidacionEntidadDTORespuesta> obtenerPorPeriodo(Long periodoId);
    
    List<LiquidacionEntidadDTORespuesta> obtenerPorEstado(String estado);
}
