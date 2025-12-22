package com.unicauca.pensionados.backend.application.service.interfaces;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.resolucion.ResolucionDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.resolucion.ResolucionResponseDTO;

public interface IResolucionServicio {
    
    ResolucionResponseDTO crear(ResolucionDTOPeticion peticion);
    
    ResolucionResponseDTO actualizar(Long id, ResolucionDTOPeticion peticion);
    
    void eliminar(Long id);
    
    ResolucionResponseDTO obtenerPorId(Long id);
    
    List<ResolucionResponseDTO> obtenerTodas();
    
    List<ResolucionResponseDTO> obtenerPorPensionado(Long pensionadoId);
    
    List<ResolucionResponseDTO> obtenerPorEstado(String estado);
    
    ResolucionResponseDTO obtenerPorNumero(String numeroResolucion);
}
