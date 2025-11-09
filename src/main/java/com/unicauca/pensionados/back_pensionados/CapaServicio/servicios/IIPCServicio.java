package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroIPCPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.IPCRespuestaDTO;
import java.util.List;

public interface IIPCServicio {
    List<IPCRespuestaDTO> listarIPC();
    IPCRespuestaDTO buscarIPCPorAnio(Integer anio);
    void registrarIPC(RegistroIPCPeticion peticion);
    void actualizarIPC(Integer anio, RegistroIPCPeticion peticion);
    void eliminarIPC(Integer anio);
}
