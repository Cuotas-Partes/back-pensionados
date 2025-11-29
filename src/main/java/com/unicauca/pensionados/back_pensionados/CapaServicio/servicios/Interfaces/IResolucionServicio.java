package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Interfaces;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.ResolucionDTOPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResolucionDTORespuesta;

public interface IResolucionServicio {
    
    ResolucionDTORespuesta crear(ResolucionDTOPeticion peticion);
    
    ResolucionDTORespuesta actualizar(Long id, ResolucionDTOPeticion peticion);
    
    void eliminar(Long id);
    
    ResolucionDTORespuesta obtenerPorId(Long id);
    
    List<ResolucionDTORespuesta> obtenerTodas();
    
    List<ResolucionDTORespuesta> obtenerPorPensionado(Long pensionadoId);
    
    List<ResolucionDTORespuesta> obtenerPorEstado(String estado);
    
    ResolucionDTORespuesta obtenerPorNumero(String numeroResolucion);
}
