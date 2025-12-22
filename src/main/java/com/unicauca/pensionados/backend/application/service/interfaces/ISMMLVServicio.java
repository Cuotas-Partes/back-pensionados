package com.unicauca.pensionados.backend.application.service.interfaces;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.smmlv.SMMLVDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.smmlv.SMMLVDTORespuesta;

public interface ISMMLVServicio {
    
    SMMLVDTORespuesta registrarSMMLV(SMMLVDTOPeticion peticion);
    
    SMMLVDTORespuesta actualizarSMMLV(Long id, SMMLVDTOPeticion peticion);
    
    void eliminarSMMLV(Long id);
    
    SMMLVDTORespuesta obtenerPorId(Long id);
    
    SMMLVDTORespuesta obtenerPorAno(Integer ano);
    
    List<SMMLVDTORespuesta> listarSMMLV();
}
