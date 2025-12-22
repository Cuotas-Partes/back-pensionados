package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.request.cuota.RegistroCuotaPagarPeticion;
import com.unicauca.pensionados.backend.application.dto.response.cuota.CuotaPagarRespuestaDTO;

import java.util.List;

public interface ICuotaPagarServicio {
    CuotaPagarRespuestaDTO crear(RegistroCuotaPagarPeticion peticion);
    CuotaPagarRespuestaDTO actualizar(Long id, RegistroCuotaPagarPeticion peticion);
    void eliminar(Long id);

    CuotaPagarRespuestaDTO obtenerPorId(Long id);
    List<CuotaPagarRespuestaDTO> listar();
}
