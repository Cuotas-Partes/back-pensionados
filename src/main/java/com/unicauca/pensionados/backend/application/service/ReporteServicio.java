package com.unicauca.pensionados.backend.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.application.service.interfaces.IReporteServicio;
import com.unicauca.pensionados.backend.domain.model.entity.Reporte;
import com.unicauca.pensionados.backend.domain.model.entity.Usuario;
import com.unicauca.pensionados.backend.domain.model.enums.TipoReporte;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.ReporteRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.UsuarioRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.reporte.ReporteDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.ReporteDTORespuesta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteServicio implements IReporteServicio {

    private final ReporteRepositorio reporteRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    @Transactional
    public ReporteDTORespuesta crear(ReporteDTOPeticion peticion) {
        Usuario usuario = null;
        if (peticion.getGeneradoPor() != null) {
            usuario = usuarioRepositorio.findByUsername(peticion.getGeneradoPor())
                    .orElse(null);
        }

        Reporte reporte = new Reporte();
        reporte.setTitulo(peticion.getTitulo());
        reporte.setTipo(peticion.getTipo());
        reporte.setFiltros(peticion.getFiltros());
        reporte.setDatos(peticion.getDatos());
        reporte.setTotalRegistros(0);
        reporte.setTotalValor(BigDecimal.ZERO);
        reporte.setTotalCorriente(BigDecimal.ZERO);
        reporte.setTotalNoCorriente(BigDecimal.ZERO);
        reporte.setTotalPrescrito(BigDecimal.ZERO);
        reporte.setGeneradoEn(LocalDateTime.now());
        reporte.setUsuario(usuario);

        reporte = reporteRepositorio.save(reporte);
        return mapearARespuesta(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteDTORespuesta obtenerPorId(Long id) {
        Reporte reporte = reporteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Reporte no encontrado"));
        return mapearARespuesta(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTORespuesta> obtenerTodos() {
        return reporteRepositorio.findAll().stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTORespuesta> obtenerPorTipo(String tipo) {
        TipoReporte tipoEnum = TipoReporte.valueOf(tipo.toUpperCase());
        return reporteRepositorio.findByTipo(tipoEnum).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTORespuesta> obtenerPorUsuario(Integer usuarioId) {
        return reporteRepositorio.findByUsuarioId(usuarioId).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!reporteRepositorio.existsById(id)) {
            throw new RecursoNoEncontrado("Reporte no encontrado");
        }
        reporteRepositorio.deleteById(id);
    }

    private ReporteDTORespuesta mapearARespuesta(Reporte reporte) {
        ReporteDTORespuesta respuesta = new ReporteDTORespuesta();
        respuesta.setId(reporte.getId());
        respuesta.setTitulo(reporte.getTitulo());
        respuesta.setTipo(reporte.getTipo());
        respuesta.setFiltros(reporte.getFiltros());
        respuesta.setDatos(reporte.getDatos());
        respuesta.setTotalRegistros(reporte.getTotalRegistros());
        respuesta.setTotalValor(reporte.getTotalValor());
        respuesta.setTotalCorriente(reporte.getTotalCorriente());
        respuesta.setTotalNoCorriente(reporte.getTotalNoCorriente());
        respuesta.setTotalPrescrito(reporte.getTotalPrescrito());
        respuesta.setGeneradoEn(reporte.getGeneradoEn());
        if (reporte.getUsuario() != null) {
            respuesta.setGeneradoPor(reporte.getUsuario().getUsername());
            respuesta.setUsuarioId(reporte.getUsuario().getId());
        }
        return respuesta;
    }
}
