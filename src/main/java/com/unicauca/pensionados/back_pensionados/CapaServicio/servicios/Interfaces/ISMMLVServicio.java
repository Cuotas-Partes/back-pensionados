package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Interfaces;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.SMMLVDTOPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.SMMLVDTORespuesta;

public interface ISMMLVServicio {
    
    SMMLVDTORespuesta crear(SMMLVDTOPeticion peticion);
    
    SMMLVDTORespuesta actualizar(Long id, SMMLVDTOPeticion peticion);
    
    void eliminar(Long id);
    
    SMMLVDTORespuesta obtenerPorId(Long id);
    
    SMMLVDTORespuesta obtenerPorAno(Integer ano);
    
    List<SMMLVDTORespuesta> obtenerTodos();
}
