package com.unicauca.pensionados.backend.domain.model.mappers.pensionado;

import com.unicauca.pensionados.backend.application.dto.response.pensionado.PensionadoDTO;
import com.unicauca.pensionados.backend.application.dto.response.sucesor.SucesorRespuesta;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Sucesor;
import com.unicauca.pensionados.backend.domain.model.mappers.resoluciones.ResolucionesMapper;

import java.util.List;

public class PensionadoMapper {

    public static PensionadoDTO toDTO(Pensionado p) {
        return PensionadoDTO.builder()
                .idPensionado(p.getIdPensionado())
                .cedula(p.getCedula())
                .fechaExpedicionCedula(p.getFechaExpedicionCedula())
                .nombre(p.getNombre())
                .apellidos(p.getApellidos())
                .fechaNacimiento(p.getFechaNacimiento())
                .telefono(p.getTelefono())
                .correo(p.getCorreo())
                .entidadJubilacion(p.getEntidadJubilacion())
                .entityId(p.getEntityId())
                .diasTrabajadosEntidad(p.getDiasTrabajadosEntidad())
                .diasTotalesTrabajados(p.getDiasTotalesTrabajados())
                .porcentajeCuota(p.getPorcentajeCuota())
                .tipoJubilacion(p.getTipoJubilacion())
                .valorPensionActual(p.getValorPensionActual())
                .estado(p.getEstado())
                .fechaFallecimiento(p.getFechaFallecimiento())
                .tieneSustituto(p.getTieneSustituto())
                .cuotasPendientes(p.getCuotasPendientes())
                .totalPendiente(p.getTotalPendiente())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .resoluciones(p.getResoluciones() == null
                        ? List.of()
                        : p.getResoluciones().stream()
                        .map(ResolucionesMapper::toResponseDTO)
                        .toList())
                .sustitutos(p.getSustitutos() == null
                        ? List.of()
                        : p.getSustitutos().stream()
                        .map(PensionadoMapper::toSustitutoDTO)
                        .toList())
                .build();
    }

    public static SucesorRespuesta toSustitutoDTO(Sucesor s) {
        return new SucesorRespuesta(
                s.getNumeroDocumento(),
                s.getTipoDocumento(),
                s.getNombreCompleto(),
                s.getEstado(),
                s.getFechaInicio(),
                s.getFechaFin(),
                s.getPorcentajePension(),
                s.getResolucionNombramiento() != null
                        ? s.getResolucionNombramiento().getId()
                        : null,
                s.getResolucionNombramiento() != null
                        ? s.getResolucionNombramiento().getNumeroResolucion()
                        : null
        );
    }
}
