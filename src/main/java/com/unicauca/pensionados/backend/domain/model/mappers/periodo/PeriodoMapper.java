package com.unicauca.pensionados.backend.domain.model.mappers.periodo;

import com.unicauca.pensionados.backend.application.dto.response.PeriodoRespuesta;
import com.unicauca.pensionados.backend.domain.model.entity.Periodo;

import java.math.BigDecimal;

public class PeriodoMapper {

    private PeriodoMapper() {
    }

    public static PeriodoRespuesta toDTO(Periodo periodo) {
        if (periodo == null) {
            return null;
        }

        // PeriodoRespuesta usa primitivos/diferentes tipos en algunos campos.
        // Mantenemos conversión segura sin tocar el DTO existente.
        Double ipcAsDouble = periodo.getIpc() != null ? periodo.getIpc().doubleValue() : null;
        BigDecimal cuotaParteTotal = periodo.getCuotaParteTotalPeriodo();

        return PeriodoRespuesta.builder()
                .anio(periodo.getAnio() != null ? periodo.getAnio() : 0)
                .fechaInicioPeriodo(periodo.getFechaInicioPeriodo())
                .fechaFinPeriodo(periodo.getFechaFinPeriodo())
                .ipc(ipcAsDouble)
                .cuotaParteTotalPeriodo(cuotaParteTotal)
                .estadoPeriodo(periodo.getEstadoPeriodo())
                .build();
    }
}
