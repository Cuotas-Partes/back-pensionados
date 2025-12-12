package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.response.DTFDTO;

import java.util.List;

public interface IDTFServicio {

    DTFDTO guardarDTF(DTFDTO dtf);
    DTFDTO actualizarDTF(DTFDTO dtf);
    void eliminarDTF(Long id);

    DTFDTO obtenerDTFPorId(Long id);
    List<DTFDTO> obtenerDTFPorMesAnio(Long mes, Long anio);

    List<DTFDTO> listarDTFs();
}
