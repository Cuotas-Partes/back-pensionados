package com.unicauca.pensionados.backend.application.service.interfaces;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.LiquidacionPensionadoDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.LiquidacionPensionadoDTORespuesta;

public interface ILiquidacionPensionadoServicio {
    
    LiquidacionPensionadoDTORespuesta crear(LiquidacionPensionadoDTOPeticion peticion);
    
    LiquidacionPensionadoDTORespuesta actualizar(Long id, LiquidacionPensionadoDTOPeticion peticion);
    
    void eliminar(Long id);
    
    LiquidacionPensionadoDTORespuesta obtenerPorId(Long id);
    
    List<LiquidacionPensionadoDTORespuesta> obtenerTodas();
    
    List<LiquidacionPensionadoDTORespuesta> obtenerPorPensionado(Long pensionadoId);
    
    List<LiquidacionPensionadoDTORespuesta> obtenerPorPeriodo(Long periodoId);
    
    List<LiquidacionPensionadoDTORespuesta> obtenerPorEstado(String estado);
}
