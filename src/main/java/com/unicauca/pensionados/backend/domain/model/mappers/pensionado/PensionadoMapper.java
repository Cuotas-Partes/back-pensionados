package com.unicauca.pensionados.backend.domain.model.mappers.pensionado;

import com.unicauca.pensionados.backend.application.dto.response.pensionado.PensionadoDTO;
import com.unicauca.pensionados.backend.application.dto.response.resolucion.ResolucionResponseDTO;
import com.unicauca.pensionados.backend.application.dto.response.sucesor.SucesorRespuesta;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Resolucion;
import com.unicauca.pensionados.backend.domain.model.entity.Sucesor;

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
                p.getValorPensionActual(),
                p.getEstado(),
                p.getFechaFallecimiento(),
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
                        .toList(),
                p.getSustitutos() == null
                        ? List.of()
                        : p.getSustitutos().stream()
                        .map(PensionadoMapper::toSustitutoDTO)
                        .toList()
        );
    }

    public static ResolucionResponseDTO toResolucionDTO(Resolucion r) {
        return new ResolucionResponseDTO(
                r.getId(),
                r.getNumeroResolucion(),
                r.getFechaResolucion(),
                r.getValorResolucion(),
                r.getEstado(),
                r.getTipoResolucion(),
                r.getObservaciones(),
                r.getDatosEspecificos(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
    public static SucesorRespuesta toSustitutoDTO(Sucesor s) {
        return new SucesorRespuesta(
                s.getNumeroDocumento(),
                s.getTipoIdentificacion(),
                s.getNombreCompleto(),
                s.getTelefono(),
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
