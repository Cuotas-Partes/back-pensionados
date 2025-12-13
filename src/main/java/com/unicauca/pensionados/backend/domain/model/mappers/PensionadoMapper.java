package com.unicauca.pensionados.backend.domain.model.mappers;

import com.unicauca.pensionados.backend.application.dto.response.PensionadoDTO;
import com.unicauca.pensionados.backend.application.dto.response.ResolucionDTORespuesta;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Resolucion;

import java.util.List;

public class PensionadoMapper {

    public static PensionadoDTO toDTO(Pensionado p) {
        return new PensionadoDTO(
                p.getIdPersona(),
                p.getCedula(),
                p.getFechaExpedicionCedula(),
                p.getNombre(),
                p.getApellidos(),
                p.getFechaNacimiento(),
                p.getTelefono(),
                p.getCorreo(),
                p.getEntidadJubilacion(),
                p.getEntityNit(),
                p.getEntityId(),
                p.getDiasTrabajadosEntidad(),
                p.getDiasTotalesTrabajados(),
                p.getPorcentajeCuota(),
                p.getTipoJubilacion(),
                p.getValorPension(),
                p.getEstado(),
                p.getFechaFallecimiento(),
                p.getPensionadoSustituido(),
                p.getTieneSustituto(),
                p.getSustitutoId(),
                p.getCuotasPendientes(),
                p.getTotalPendiente(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.getResoluciones() == null
                        ? List.of()
                        : p.getResoluciones().stream()
                        .map(PensionadoMapper::toResolucionDTO)
                        .toList()
        );
    }

    public static ResolucionDTORespuesta toResolucionDTO(Resolucion r) {
        return new ResolucionDTORespuesta(
                r.getId(),
                r.getNumeroResolucion(),
                r.getFechaResolucion(),
                r.getValorResolucion(),
                r.getEstado(),
                r.getTipoResolucion()
        );
    }
}
