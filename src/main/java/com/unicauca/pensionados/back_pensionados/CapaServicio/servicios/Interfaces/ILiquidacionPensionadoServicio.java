package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Interfaces;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.LiquidacionPensionadoDTOPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.LiquidacionPensionadoDTORespuesta;

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
