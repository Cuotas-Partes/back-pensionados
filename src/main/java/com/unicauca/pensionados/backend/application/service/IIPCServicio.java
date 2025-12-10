package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.application.dto.request.RegistroIPCPeticion;
import com.unicauca.pensionados.backend.application.dto.response.IPCRespuestaDTO;
import java.util.List;

public interface IIPCServicio {
    List<IPCRespuestaDTO> listarIPC();
    IPCRespuestaDTO buscarIPCPorAnio(Integer anio);
    void registrarIPC(RegistroIPCPeticion peticion);
    void actualizarIPC(Integer anio, RegistroIPCPeticion peticion);
    void eliminarIPC(Integer anio);
}
