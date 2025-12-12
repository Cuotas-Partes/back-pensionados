package com.unicauca.pensionados.backend.application.service.interfaces;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.SMMLVDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.SMMLVDTORespuesta;

public interface ISMMLVServicio {
    
    SMMLVDTORespuesta crear(SMMLVDTOPeticion peticion);
    
    SMMLVDTORespuesta actualizar(Long id, SMMLVDTOPeticion peticion);
    
    void eliminar(Long id);
    
    SMMLVDTORespuesta obtenerPorId(Long id);
    
    SMMLVDTORespuesta obtenerPorAno(Integer ano);
    
    List<SMMLVDTORespuesta> obtenerTodos();
}
