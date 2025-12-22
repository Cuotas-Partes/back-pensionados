package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.request.dtf.DtfRequestDTO;
import com.unicauca.pensionados.backend.application.dto.response.dtf.DTFDTO;
import com.unicauca.pensionados.backend.domain.model.entity.DTF;

import java.util.List;

public interface IDTFServicio {

    DTFDTO guardarDTF(DtfRequestDTO dtf);
    DTFDTO actualizarDTF(Long id,DtfRequestDTO dtf);
    void eliminarDTF(Long id);

    DTFDTO obtenerDTFPorId(Long id);
    List<DTFDTO> obtenerPorPeriodo(String periodo);
    List<DTFDTO> listarDTFs();
}
