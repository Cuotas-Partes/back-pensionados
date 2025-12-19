package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.application.dto.request.cuota.DetalleCuotaPeticion;
import com.unicauca.pensionados.backend.application.dto.request.cuota.RegistroCuotaCobrarPeticion;
import com.unicauca.pensionados.backend.application.dto.response.cuota.CuotaCobrarRespuestaDTO;
import com.unicauca.pensionados.backend.application.service.interfaces.ICuotaCobrarServicio;
import com.unicauca.pensionados.backend.application.service.util.CalculadoraCuotas;
import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.domain.model.entity.CuotaCobrar;
import com.unicauca.pensionados.backend.domain.model.entity.DetalleCuota;
import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Periodo;
import com.unicauca.pensionados.backend.domain.model.mappers.cuota.CuotaCobrarMapper;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.CuotaCobrarRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PeriodoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CuotaCobrarServicio implements ICuotaCobrarServicio {

    private final CuotaCobrarRepositorio cuotaCobrarRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final PeriodoRepositorio periodoRepositorio;

    public CuotaCobrarServicio(CuotaCobrarRepositorio cuotaCobrarRepositorio,
                               PensionadoRepositorio pensionadoRepositorio,
                               EntidadRepositorio entidadRepositorio,
                               PeriodoRepositorio periodoRepositorio) {
        this.cuotaCobrarRepositorio = cuotaCobrarRepositorio;
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadRepositorio = entidadRepositorio;
        this.periodoRepositorio = periodoRepositorio;
    }

    @Transactional
    @Override
    public CuotaCobrarRespuestaDTO crear(RegistroCuotaCobrarPeticion peticion) {
        CuotaCobrar cuota = new CuotaCobrar();
        aplicar(cuota, peticion);
        cuota = cuotaCobrarRepositorio.save(cuota);
        return CuotaCobrarMapper.toDTO(cuota);
    }

    @Transactional
    @Override
    public CuotaCobrarRespuestaDTO actualizar(Long id, RegistroCuotaCobrarPeticion peticion) {
        CuotaCobrar cuota = cuotaCobrarRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la cuota por cobrar con ID: " + id));
        aplicar(cuota, peticion);
        cuota = cuotaCobrarRepositorio.save(cuota);
        return CuotaCobrarMapper.toDTO(cuota);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        CuotaCobrar cuota = cuotaCobrarRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la cuota por cobrar con ID: " + id));
        cuotaCobrarRepositorio.delete(cuota);
    }

    @Override
    public CuotaCobrarRespuestaDTO obtenerPorId(Long id) {
        CuotaCobrar cuota = cuotaCobrarRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la cuota por cobrar con ID: " + id));
        return CuotaCobrarMapper.toDTO(cuota);
    }

    @Override
    public List<CuotaCobrarRespuestaDTO> listar() {
        return cuotaCobrarRepositorio.findAll().stream().map(CuotaCobrarMapper::toDTO).toList();
    }

    private void aplicar(CuotaCobrar cuota, RegistroCuotaCobrarPeticion p) {
        Pensionado pensionado = pensionadoRepositorio.findById(p.getPensionadoId())
                .orElseThrow(() -> new RecursoNoEncontrado("Pensionado no encontrado con ID: " + p.getPensionadoId()));
        Entidad entidad = entidadRepositorio.findById(p.getEntidadId())
                .orElseThrow(() -> new RecursoNoEncontrado("Entidad no encontrada con ID: " + p.getEntidadId()));
        Periodo periodo = periodoRepositorio.findById(p.getPeriodoId())
                .orElseThrow(() -> new RecursoNoEncontrado("Periodo no encontrado con ID: " + p.getPeriodoId()));

        cuota.setPensionado(pensionado);
        cuota.setEntidad(entidad);
        cuota.setPeriodo(periodo);

        // Asignar datos básicos proporcionados por el cliente
        cuota.setFechaInicio(p.getFechaInicio());
        cuota.setFechaLiquidacion(p.getFechaLiquidacion());
        cuota.setDiasTotales(p.getDiasTotales());
        cuota.setCantidadCuotas(p.getCantidadCuotas());
        cuota.setAjuste(p.getAjuste() != null ? p.getAjuste() : java.math.BigDecimal.ZERO);
        cuota.setEsReliquidacion(p.getEsReliquidacion());
        cuota.setComentarios(p.getComentarios());
        cuota.setPorcentajeUniversidad(p.getPorcentajeUniversidad());

        // ========== CÁLCULOS AUTOMÁTICOS ==========

        // 1. Calcular porcentaje de cuota parte (si no viene en la petición)
        if (p.getValorCuotaParte() != null) {
            // Si el cliente proporciona el valor directamente, lo usamos
            cuota.setValorCuotaParte(p.getValorCuotaParte());
        } else {
            // Calcular desde los datos del pensionado
            // % Cuota Parte = (Días Entidad / Total Días) × 100
            java.math.BigDecimal porcentajeCuotaParte = CalculadoraCuotas.calcularPorcentajeCuotaParte(
                pensionado.getDiasTrabajadosEntidad(),
                pensionado.getDiasTotalesTrabajados()
            );

            // Valor Cuota Parte = Valor Pensión × (% / 100)
            java.math.BigDecimal valorCuotaParte = CalculadoraCuotas.calcularValorCuotaParte(
                pensionado.getValorPensionActual(),
                porcentajeCuotaParte
            );
            cuota.setValorCuotaParte(valorCuotaParte);
        }

        // 2. Calcular el total de cuotas (valor mensual × cantidad de meses)
        java.math.BigDecimal valorCuotasTotal = CalculadoraCuotas.calcularValorCuotasTotal(
            cuota.getValorCuotaParte(),
            cuota.getCantidadCuotas()
        );
        cuota.setValorCuotasTotal(valorCuotasTotal);

        // 3. Calcular el total a cobrar (cuotas + ajuste)
        // NO se calculan intereses en cuotas normales
        java.math.BigDecimal totalCobrar = CalculadoraCuotas.calcularTotal(
            valorCuotasTotal,
            java.math.BigDecimal.ZERO,  // Sin intereses
            cuota.getAjuste()
        );
        cuota.setTotalCobrar(totalCobrar);

        // 4. Calcular el valor que paga la universidad (basado en porcentaje)
        java.math.BigDecimal valorPagaUniversidad = CalculadoraCuotas.calcularValorPorPorcentaje(
            totalCobrar,
            cuota.getPorcentajeUniversidad()
        );
        cuota.setValorPagaUniversidad(valorPagaUniversidad);

        if (p.getEstado() != null) {
            cuota.setEstado(p.getEstado());
        }

        // Reemplazo total de detalles
        if (cuota.getDetallesCuotas() == null) {
            cuota.setDetallesCuotas(new ArrayList<>());
        } else {
            cuota.getDetallesCuotas().clear();
        }

        if (p.getDetallesCuotas() != null) {
            for (DetalleCuotaPeticion det : p.getDetallesCuotas()) {
                DetalleCuota d = new DetalleCuota();
                d.setCuotaCobrar(cuota);
                d.setCuotaPagar(null);
                d.setDescripcion(det.getDescripcion());
                d.setValor(det.getValor());
                cuota.getDetallesCuotas().add(d);
            }
        }
    }

}
