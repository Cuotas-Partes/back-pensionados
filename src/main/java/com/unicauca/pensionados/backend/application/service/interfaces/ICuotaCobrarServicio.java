package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.request.cuota.RegistroCuotaCobrarPeticion;
import com.unicauca.pensionados.backend.application.dto.response.cuota.CuotaCobrarRespuestaDTO;

import java.util.List;

public interface ICuotaCobrarServicio {
    CuotaCobrarRespuestaDTO crear(RegistroCuotaCobrarPeticion peticion);
    CuotaCobrarRespuestaDTO actualizar(Long id, RegistroCuotaCobrarPeticion peticion);
    void eliminar(Long id);

    CuotaCobrarRespuestaDTO obtenerPorId(Long id);
    List<CuotaCobrarRespuestaDTO> listar();
}
