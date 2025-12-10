package com.unicauca.pensionados.backend.application.service;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.ResolucionDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.ResolucionDTORespuesta;

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
