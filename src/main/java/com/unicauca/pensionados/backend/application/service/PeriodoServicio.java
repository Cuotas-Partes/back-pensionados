package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.application.dto.request.periodo.RegistroPeriodoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.PeriodoRespuesta;
import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.application.service.interfaces.IPeriodoServicio;
import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.domain.model.entity.Periodo;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPeriodo;
import com.unicauca.pensionados.backend.domain.model.mappers.periodo.PeriodoMapper;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PeriodoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PeriodoServicio implements IPeriodoServicio {

    private final PeriodoRepositorio periodoRepositorio;

    @Autowired
    private ILogCambioServicio logCambioServicio;

    private final String nombreEntidad = "PERIODO";

    public PeriodoServicio(PeriodoRepositorio periodoRepositorio) {
        this.periodoRepositorio = periodoRepositorio;
    }

    @Transactional
    @Override
    public PeriodoRespuesta crear(RegistroPeriodoPeticion peticion) {
        Periodo periodo = new Periodo();
        aplicar(periodo, peticion);
        periodo = periodoRepositorio.save(periodo);
        logCambioServicio.registrarCreacion(nombreEntidad, periodo);
        return PeriodoMapper.toDTO(periodo);
    }

    @Transactional
    @Override
    public PeriodoRespuesta actualizar(Long id, RegistroPeriodoPeticion peticion) {
        Periodo periodo = periodoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró el periodo con ID: " + id));

        aplicar(periodo, peticion);
        periodo = periodoRepositorio.save(periodo);
        logCambioServicio.registrarActualizacion(nombreEntidad, periodo, periodo);
        return PeriodoMapper.toDTO(periodo);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        Periodo periodo = periodoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró el periodo con ID: " + id));
        periodoRepositorio.delete(periodo);
        logCambioServicio.registrarEliminacion(nombreEntidad, periodo);
    }

    @Override
    public PeriodoRespuesta obtenerPorId(Long id) {
        Periodo periodo = periodoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró el periodo con ID: " + id));
        logCambioServicio.registrarConsulta(nombreEntidad);
        return PeriodoMapper.toDTO(periodo);
    }

    @Override
    public List<PeriodoRespuesta> listar() {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return periodoRepositorio.findAll().stream().map(PeriodoMapper::toDTO).toList();
    }

    private void aplicar(Periodo periodo, RegistroPeriodoPeticion peticion) {
        periodo.setAnio(peticion.getAnio());
        periodo.setFechaInicioPeriodo(peticion.getFechaInicioPeriodo());
        periodo.setFechaFinPeriodo(peticion.getFechaFinPeriodo());
        periodo.setIpc(peticion.getIpc());
        periodo.setCuotaParteTotalPeriodo(peticion.getCuotaParteTotalPeriodo() != null ? peticion.getCuotaParteTotalPeriodo() : BigDecimal.ZERO);
        periodo.setEstadoPeriodo(peticion.getEstadoPeriodo() != null ? peticion.getEstadoPeriodo() : EstadoPeriodo.ACTIVO);
    }
}
