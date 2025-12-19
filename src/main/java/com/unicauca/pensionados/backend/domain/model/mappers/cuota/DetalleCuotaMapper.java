package com.unicauca.pensionados.backend.domain.model.mappers.cuota;

import com.unicauca.pensionados.backend.application.dto.response.cuota.DetalleCuotaRespuestaDTO;
import com.unicauca.pensionados.backend.domain.model.entity.DetalleCuota;

public class DetalleCuotaMapper {

    private DetalleCuotaMapper() {
    }

    public static DetalleCuotaRespuestaDTO toDTO(DetalleCuota detalle) {
        if (detalle == null) {
            return null;
        }

        return DetalleCuotaRespuestaDTO.builder()
                .id(detalle.getId())
                .descripcion(detalle.getDescripcion())
                .valor(detalle.getValor())
                .build();
    }
}
