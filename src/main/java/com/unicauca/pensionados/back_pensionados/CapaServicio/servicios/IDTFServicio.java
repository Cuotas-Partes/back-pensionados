package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DTFDTO;

import java.util.List;

public interface IDTFServicio {

    DTFDTO guardarDTF(DTFDTO dtf);
    DTFDTO actualizarDTF(DTFDTO dtf);
    void eliminarDTF(Long id);

    DTFDTO obtenerDTFPorId(Long id);
    List<DTFDTO> obtenerDTFPorMesAnio(Long mes, Long anio);

    List<DTFDTO> listarDTFs();
}
