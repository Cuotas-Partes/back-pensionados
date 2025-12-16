package com.unicauca.pensionados.backend.domain.model.mappers.resoluciones;

import com.unicauca.pensionados.backend.application.dto.request.resolucion.*;
import com.unicauca.pensionados.backend.application.dto.response.resolucion.ResolucionResponseDTO;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Resolucion;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;

import java.util.HashMap;
import java.util.Map;

public class ResolucionesMapper {
    private ResolucionesMapper() {}

    public static Resolucion toEntity(
            ResolucionBaseRequestDTO dto,
            Pensionado pensionado
    ) {

        Resolucion r = new Resolucion();
        r.setNumeroResolucion(dto.getNumeroResolucion());
        r.setFechaResolucion(dto.getFechaResolucion());
        r.setValorResolucion(dto.getValorResolucion());
        r.setTipoResolucion(dto.getTipoResolucion());
        r.setObservaciones(dto.getObservaciones());
        r.setEstado(EstadoResolucion.VIGENTE);
        r.setPensionado(pensionado);

        Map<String, Object> datos = new HashMap<>();

        switch (dto.getTipoResolucion()) {

            case PENSION_INICIAL -> {
                PensionInicialRequestDTO d =
                        (PensionInicialRequestDTO) dto;
                datos.put("tipoPension", d.getTipoPension());
            }

            case AUMENTO_PENSION -> {
                AumentoPensionRequestDTO d =
                        (AumentoPensionRequestDTO) dto;
                datos.put("porcentajeAumento", d.getPorcentajeAumento());
            }

            case NOMBRAMIENTO_SUSTITUTO -> {
                NombramientoSustitutoRequestDTO d =
                        (NombramientoSustitutoRequestDTO) dto;
                datos.put("sustitutoId", d.getSustitutoId());
            }
        }

        r.setDatosEspecificos(datos);
        return r;
    }

    public static ResolucionResponseDTO toResponseDTO(Resolucion r) {
        return ResolucionResponseDTO.builder()
                .id(r.getId())
                .numeroResolucion(r.getNumeroResolucion())
                .fechaResolucion(r.getFechaResolucion())
                .valorResolucion(r.getValorResolucion())
                .estado(r.getEstado())
                .tipoResolucion(r.getTipoResolucion())
                .observaciones(r.getObservaciones())
                .datosEspecificos(r.getDatosEspecificos())
                .build();
    }
}
