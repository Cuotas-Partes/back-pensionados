package com.unicauca.pensionados.backend.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.application.service.interfaces.ILiquidacionPensionadoServicio;
import com.unicauca.pensionados.backend.domain.model.entity.LiquidacionPensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Periodo;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoLiquidacion;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.LiquidacionPensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PeriodoRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.LiquidacionPensionadoDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.LiquidacionPensionadoDTORespuesta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiquidacionPensionadoServicio implements ILiquidacionPensionadoServicio {

    private final LiquidacionPensionadoRepositorio liquidacionRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final PeriodoRepositorio periodoRepositorio;

    @Override
    @Transactional
    public LiquidacionPensionadoDTORespuesta crear(LiquidacionPensionadoDTOPeticion peticion) {
        Pensionado pensionado = pensionadoRepositorio.findById(peticion.getPensionadoId())
                .orElseThrow(() -> new RecursoNoEncontrado("Pensionado no encontrado"));

        Periodo periodo = periodoRepositorio.findById(peticion.getPeriodoId())
                .orElseThrow(() -> new RecursoNoEncontrado("Periodo no encontrado"));

        LiquidacionPensionado liquidacion = new LiquidacionPensionado();
        liquidacion.setPensionado(pensionado);
        liquidacion.setPensionadoCedula(String.valueOf(pensionado.getCedula()));
        liquidacion.setPensionadoNombre(pensionado.getNombre() + " " + pensionado.getApellidos());
        liquidacion.setPeriodo(periodo);
        liquidacion.setPeriodoNombre(periodo.getNombrePeriodo());
        liquidacion.setTotalAPagar(peticion.getTotalAPagar());
        liquidacion.setValorCorriente(peticion.getValorCorriente());
        liquidacion.setValorNoCorriente(peticion.getValorNoCorriente());
        liquidacion.setValorPrescrito(peticion.getValorPrescrito());
        liquidacion.setDescuentos(peticion.getDescuentos());
        liquidacion.setNetoPagable(peticion.getNetoPagable());
        liquidacion.setEstado(peticion.getEstado());
        liquidacion.setFechaLiquidacion(peticion.getFechaLiquidacion());
        liquidacion.setFechaPago(peticion.getFechaPago());
        liquidacion.setDetalles(peticion.getDetalles());
        liquidacion.setCreatedAt(LocalDateTime.now());

        liquidacion = liquidacionRepositorio.save(liquidacion);
        return mapearARespuesta(liquidacion);
    }

    @Override
    @Transactional
    public LiquidacionPensionadoDTORespuesta actualizar(Long id, LiquidacionPensionadoDTOPeticion peticion) {
        LiquidacionPensionado liquidacion = liquidacionRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Liquidación no encontrada"));

        liquidacion.setTotalAPagar(peticion.getTotalAPagar());
        liquidacion.setValorCorriente(peticion.getValorCorriente());
        liquidacion.setValorNoCorriente(peticion.getValorNoCorriente());
        liquidacion.setValorPrescrito(peticion.getValorPrescrito());
        liquidacion.setDescuentos(peticion.getDescuentos());
        liquidacion.setNetoPagable(peticion.getNetoPagable());
        liquidacion.setEstado(peticion.getEstado());
        liquidacion.setFechaLiquidacion(peticion.getFechaLiquidacion());
        liquidacion.setFechaPago(peticion.getFechaPago());
        liquidacion.setDetalles(peticion.getDetalles());
        liquidacion.setUpdatedAt(LocalDateTime.now());

        liquidacion = liquidacionRepositorio.save(liquidacion);
        return mapearARespuesta(liquidacion);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!liquidacionRepositorio.existsById(id)) {
            throw new RecursoNoEncontrado("Liquidación no encontrada");
        }
        liquidacionRepositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LiquidacionPensionadoDTORespuesta obtenerPorId(Long id) {
        LiquidacionPensionado liquidacion = liquidacionRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Liquidación no encontrada"));
        return mapearARespuesta(liquidacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionPensionadoDTORespuesta> obtenerTodas() {
        return liquidacionRepositorio.findAll().stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionPensionadoDTORespuesta> obtenerPorPensionado(Long pensionadoId) {
        return liquidacionRepositorio.findByPensionadoIdPersona(pensionadoId).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionPensionadoDTORespuesta> obtenerPorPeriodo(Long periodoId) {
        return liquidacionRepositorio.findByPeriodoIdPeriodo(periodoId).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionPensionadoDTORespuesta> obtenerPorEstado(String estado) {
        EstadoLiquidacion estadoEnum = EstadoLiquidacion.valueOf(estado.toUpperCase());
        return liquidacionRepositorio.findByEstado(estadoEnum).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    private LiquidacionPensionadoDTORespuesta mapearARespuesta(LiquidacionPensionado liquidacion) {
        LiquidacionPensionadoDTORespuesta respuesta = new LiquidacionPensionadoDTORespuesta();
        respuesta.setId(liquidacion.getId());
        respuesta.setPensionadoId(liquidacion.getPensionado().getIdPersona());
        respuesta.setPensionadoCedula(String.valueOf(liquidacion.getPensionado().getCedula()));
        respuesta.setPensionadoNombre(liquidacion.getPensionado().getNombre() + " " + liquidacion.getPensionado().getApellidos());
        respuesta.setPeriodoId(liquidacion.getPeriodo().getIdPeriodo());
        respuesta.setPeriodoNombre(liquidacion.getPeriodo().getNombrePeriodo());
        respuesta.setTotalAPagar(liquidacion.getTotalAPagar());
        respuesta.setValorCorriente(liquidacion.getValorCorriente());
        respuesta.setValorNoCorriente(liquidacion.getValorNoCorriente());
        respuesta.setValorPrescrito(liquidacion.getValorPrescrito());
        respuesta.setDescuentos(liquidacion.getDescuentos());
        respuesta.setNetoPagable(liquidacion.getNetoPagable());
        respuesta.setEstado(liquidacion.getEstado());
        respuesta.setFechaLiquidacion(liquidacion.getFechaLiquidacion());
        respuesta.setFechaPago(liquidacion.getFechaPago());
        respuesta.setDetalles(liquidacion.getDetalles());
        respuesta.setCreatedAt(liquidacion.getCreatedAt());
        respuesta.setUpdatedAt(liquidacion.getUpdatedAt());
        return respuesta;
    }
}
