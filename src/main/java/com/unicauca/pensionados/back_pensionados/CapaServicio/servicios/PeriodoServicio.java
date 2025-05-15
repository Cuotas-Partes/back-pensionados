package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

import java.util.Optional;

@Service
public class PeriodoServicio implements IPeriodoServicio {

    @Autowired
    private PeriodoRepositorio periodoRepositorio;

    @Override
    public PeriodoRespuesta consultarPeriodoPorAnio(int anio) {
        // Buscar el periodo cuyo año de IPC coincida con el año solicitado
        Optional<Periodo> periodoOpt = periodoRepositorio.findAll().stream()
            .filter(p -> p.getIPC() != null && p.getIPC().getFechaIPC() != null && p.getIPC().getFechaIPC() == anio)
            .findFirst();

        Periodo periodo = periodoOpt.orElseThrow(() -> new RuntimeException("No existe periodo para el año: " + anio));

        return PeriodoRespuesta.builder()
            .anio(periodo.getIPC().getFechaIPC())
            .fechaInicioPeriodo(periodo.getFechaInicioPeriodo())
            .fechaFinPeriodo(periodo.getFechaFinPeriodo())
            .ipc(periodo.getIPC().getValorIPC() != null ? periodo.getIPC().getValorIPC().doubleValue() : null)
            .build();
    }
}
