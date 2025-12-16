package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.request.ipc.RegistroIPCPeticion;
import com.unicauca.pensionados.backend.application.dto.response.ipc.IPCRespuestaDTO;
import java.util.List;

public interface IIPCServicio {
    List<IPCRespuestaDTO> listarIPC();
    IPCRespuestaDTO buscarIPCPorAnio(Integer anio);
    void registrarIPC(RegistroIPCPeticion peticion);
    void actualizarIPC(Long id, RegistroIPCPeticion peticion);
    void eliminarIPC(Integer anio);
}
