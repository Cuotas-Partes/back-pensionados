package com.unicauca.pensionados.backend.domain.model.mappers.ipc;

import com.unicauca.pensionados.backend.application.dto.response.ipc.IPCRespuestaDTO;
import com.unicauca.pensionados.backend.domain.model.entity.IPC;

public class IpcMapper {
    public static IPCRespuestaDTO toIpcDTO (IPC ipc){
        IPCRespuestaDTO dto = new IPCRespuestaDTO();
        dto.setId(ipc.getId());
        dto.setYear(ipc.getYear());
        dto.setIpc(ipc.getIpc());
        dto.setResolution(ipc.getResolution());
        dto.setResolutionDate(ipc.getResolutionDate());
        dto.setResolutionDetails(ipc.getResolutionDetails());
        dto.setEstado(ipc.getEstado());
        return dto;
    }
}
