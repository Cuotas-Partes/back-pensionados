package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.back_pensionados.CapaServicio.excepciones.RecursoNoEncontrado;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Interfaces.ILiquidacionEntidadServicio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.LiquidacionEntidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoLiquidacion;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.LiquidacionEntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.LiquidacionEntidadDTOPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.LiquidacionEntidadDTORespuesta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiquidacionEntidadServicio implements ILiquidacionEntidadServicio {

    private final LiquidacionEntidadRepositorio liquidacionRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final PeriodoRepositorio periodoRepositorio;

    @Override
    @Transactional
    public LiquidacionEntidadDTORespuesta crear(LiquidacionEntidadDTOPeticion peticion) {
        Entidad entidad = entidadRepositorio.findById(peticion.getNitEntidad())
                .orElseThrow(() -> new RecursoNoEncontrado("Entidad no encontrada"));

        Periodo periodo = periodoRepositorio.findById(peticion.getPeriodoId())
                .orElseThrow(() -> new RecursoNoEncontrado("Periodo no encontrado"));

        LiquidacionEntidad liquidacion = new LiquidacionEntidad();
        liquidacion.setEntidad(entidad);
        liquidacion.setEntityNit(String.valueOf(entidad.getNitEntidad()));
        liquidacion.setEntityNombre(entidad.getNombreEntidad());
        liquidacion.setPeriodo(periodo);
        liquidacion.setPeriodoNombre(periodo.getNombrePeriodo());
        liquidacion.setTotalACobrar(peticion.getTotalACobrar());
        liquidacion.setValorCorriente(peticion.getValorCorriente());
        liquidacion.setValorNoCorriente(peticion.getValorNoCorriente());
        liquidacion.setValorPrescrito(peticion.getValorPrescrito());
        liquidacion.setDescuentos(peticion.getDescuentos());
        liquidacion.setNeto(peticion.getNeto());
        liquidacion.setEstado(peticion.getEstado());
        liquidacion.setFechaLiquidacion(peticion.getFechaLiquidacion());
        liquidacion.setFechaCobro(peticion.getFechaCobro());
        liquidacion.setPensionadosCount(peticion.getPensionadosCount());
        liquidacion.setDetalles(peticion.getDetalles());
        liquidacion.setCreatedAt(LocalDateTime.now());

        liquidacion = liquidacionRepositorio.save(liquidacion);
        return mapearARespuesta(liquidacion);
    }

    @Override
    @Transactional
    public LiquidacionEntidadDTORespuesta actualizar(Long id, LiquidacionEntidadDTOPeticion peticion) {
        LiquidacionEntidad liquidacion = liquidacionRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Liquidación no encontrada"));

        // Actualizar entidad si cambió
        if (!liquidacion.getEntidad().getNitEntidad().equals(peticion.getNitEntidad())) {
            Entidad entidad = entidadRepositorio.findById(peticion.getNitEntidad())
                    .orElseThrow(() -> new RecursoNoEncontrado("Entidad no encontrada"));
            liquidacion.setEntidad(entidad);
            liquidacion.setEntityNit(String.valueOf(entidad.getNitEntidad()));
            liquidacion.setEntityNombre(entidad.getNombreEntidad());
        }

        // Actualizar periodo si cambió
        if (!liquidacion.getPeriodo().getIdPeriodo().equals(peticion.getPeriodoId())) {
            Periodo periodo = periodoRepositorio.findById(peticion.getPeriodoId())
                    .orElseThrow(() -> new RecursoNoEncontrado("Periodo no encontrado"));
            liquidacion.setPeriodo(periodo);
            liquidacion.setPeriodoNombre(periodo.getNombrePeriodo());
        }

        liquidacion.setTotalACobrar(peticion.getTotalACobrar());
        liquidacion.setValorCorriente(peticion.getValorCorriente());
        liquidacion.setValorNoCorriente(peticion.getValorNoCorriente());
        liquidacion.setValorPrescrito(peticion.getValorPrescrito());
        liquidacion.setDescuentos(peticion.getDescuentos());
        liquidacion.setNeto(peticion.getNeto());
        liquidacion.setEstado(peticion.getEstado());
        liquidacion.setFechaLiquidacion(peticion.getFechaLiquidacion());
        liquidacion.setFechaCobro(peticion.getFechaCobro());
        liquidacion.setPensionadosCount(peticion.getPensionadosCount());
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
    public LiquidacionEntidadDTORespuesta obtenerPorId(Long id) {
        LiquidacionEntidad liquidacion = liquidacionRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Liquidación no encontrada"));
        return mapearARespuesta(liquidacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionEntidadDTORespuesta> obtenerTodas() {
        return liquidacionRepositorio.findAll().stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionEntidadDTORespuesta> obtenerPorEntidad(Long nitEntidad) {
        return liquidacionRepositorio.findByEntidadNitEntidad(nitEntidad).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionEntidadDTORespuesta> obtenerPorPeriodo(Long periodoId) {
        return liquidacionRepositorio.findByPeriodoIdPeriodo(periodoId).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionEntidadDTORespuesta> obtenerPorEstado(String estado) {
        EstadoLiquidacion estadoEnum = EstadoLiquidacion.valueOf(estado.toUpperCase());
        return liquidacionRepositorio.findByEstado(estadoEnum).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    private LiquidacionEntidadDTORespuesta mapearARespuesta(LiquidacionEntidad liquidacion) {
        LiquidacionEntidadDTORespuesta respuesta = new LiquidacionEntidadDTORespuesta();
        respuesta.setId(liquidacion.getId());
        respuesta.setNitEntidad(liquidacion.getEntidad().getNitEntidad());
        respuesta.setEntityNit(String.valueOf(liquidacion.getEntidad().getNitEntidad()));
        respuesta.setEntityNombre(liquidacion.getEntidad().getNombreEntidad());
        respuesta.setPeriodoId(liquidacion.getPeriodo().getIdPeriodo());
        respuesta.setPeriodoNombre(liquidacion.getPeriodo().getNombrePeriodo());
        respuesta.setTotalACobrar(liquidacion.getTotalACobrar());
        respuesta.setValorCorriente(liquidacion.getValorCorriente());
        respuesta.setValorNoCorriente(liquidacion.getValorNoCorriente());
        respuesta.setValorPrescrito(liquidacion.getValorPrescrito());
        respuesta.setDescuentos(liquidacion.getDescuentos());
        respuesta.setNeto(liquidacion.getNeto());
        respuesta.setEstado(liquidacion.getEstado());
        respuesta.setFechaLiquidacion(liquidacion.getFechaLiquidacion());
        respuesta.setFechaCobro(liquidacion.getFechaCobro());
        respuesta.setPensionadosCount(liquidacion.getPensionadosCount());
        respuesta.setDetalles(liquidacion.getDetalles());
        respuesta.setCreatedAt(liquidacion.getCreatedAt());
        respuesta.setUpdatedAt(liquidacion.getUpdatedAt());
        return respuesta;
    }
}
