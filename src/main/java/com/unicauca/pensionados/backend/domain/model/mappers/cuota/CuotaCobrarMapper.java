package com.unicauca.pensionados.backend.domain.model.mappers.cuota;

import com.unicauca.pensionados.backend.application.dto.response.cuota.CuotaCobrarRespuestaDTO;
import com.unicauca.pensionados.backend.domain.model.entity.CuotaCobrar;

import java.util.List;

public class CuotaCobrarMapper {

    private CuotaCobrarMapper() {
    }

    public static CuotaCobrarRespuestaDTO toDTO(CuotaCobrar cuota) {
        if (cuota == null) {
            return null;
        }

        var detalles = cuota.getDetallesCuotas() == null
                ? List.<com.unicauca.pensionados.backend.application.dto.response.cuota.DetalleCuotaRespuestaDTO>of()
                : cuota.getDetallesCuotas().stream().map(DetalleCuotaMapper::toDTO).toList();

        return CuotaCobrarRespuestaDTO.builder()
                .id(cuota.getId())
                .pensionadoId(cuota.getPensionado() != null ? cuota.getPensionado().getIdPersona() : null)
                .entidadId(cuota.getEntidad() != null ? cuota.getEntidad().getIdEntidad() : null)
                .periodoId(cuota.getPeriodo() != null ? cuota.getPeriodo().getIdPeriodo() : null)
                .fechaInicio(cuota.getFechaInicio())
                .fechaLiquidacion(cuota.getFechaLiquidacion())
                .diasTotales(cuota.getDiasTotales())
                .cantidadCuotas(cuota.getCantidadCuotas())
                .tasaDiaria(cuota.getTasaDiaria())
                .valorCuotaParte(cuota.getValorCuotaParte())
                .valorCuotasTotal(cuota.getValorCuotasTotal())
                .valorInteresTotal(cuota.getValorInteresTotal())
                .ajuste(cuota.getAjuste())
                .esReliquidacion(cuota.getEsReliquidacion())
                .comentarios(cuota.getComentarios())
                .porcentajeUniversidad(cuota.getPorcentajeUniversidad())
                .valorPagaUniversidad(cuota.getValorPagaUniversidad())
                .totalCobrar(cuota.getTotalCobrar())
                .estado(cuota.getEstado())
                .detallesCuotas(detalles)
                .build();
    }
}
