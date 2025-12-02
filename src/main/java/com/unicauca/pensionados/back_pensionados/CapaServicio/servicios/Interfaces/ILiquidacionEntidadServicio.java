package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Interfaces;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.LiquidacionEntidadDTOPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.LiquidacionEntidadDTORespuesta;

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
