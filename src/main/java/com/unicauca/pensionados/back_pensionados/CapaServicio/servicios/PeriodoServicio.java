package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.IPCRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.EditarPeriodoPeticion;

import java.util.Optional;

@Service
public class PeriodoServicio implements IPeriodoServicio {

    @Autowired
    private PeriodoRepositorio periodoRepositorio;

    @Autowired
    private IPCRepositorio ipcRepositorio;

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

    @Override
    @Transactional
    public void editarPeriodo(EditarPeriodoPeticion peticion) {
        Periodo periodo = periodoRepositorio.findById(peticion.getIdPeriodo())
            .orElseThrow(() -> new RuntimeException("No existe periodo con id: " + peticion.getIdPeriodo()));

        // Actualizar el IPC si es necesario
        if (peticion.getFechaIPC() != null) {
            IPC ipc = ipcRepositorio.findById(Long.valueOf(peticion.getFechaIPC()))
                .orElseThrow(() -> new RuntimeException("No existe IPC para el año: " + peticion.getFechaIPC()));
            periodo.setIPC(ipc);
        }
        
        if (peticion.getFechaInicioPeriodo() != null) {
            periodo.setFechaInicioPeriodo(peticion.getFechaInicioPeriodo());
        }
        if (peticion.getFechaFinPeriodo() != null) {
            periodo.setFechaFinPeriodo(peticion.getFechaFinPeriodo());
        }
        if (peticion.getNumeroMesadas() != null) {
            periodo.setNumeroMesadas(peticion.getNumeroMesadas());
        }
        periodoRepositorio.save(periodo);
    }
}
