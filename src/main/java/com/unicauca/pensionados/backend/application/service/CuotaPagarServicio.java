package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.application.dto.request.cuota.DetalleCuotaPeticion;
import com.unicauca.pensionados.backend.application.dto.request.cuota.RegistroCuotaPagarPeticion;
import com.unicauca.pensionados.backend.application.dto.response.cuota.CuotaPagarRespuestaDTO;
import com.unicauca.pensionados.backend.application.service.interfaces.ICuotaPagarServicio;
import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.domain.model.entity.CuotaPagar;
import com.unicauca.pensionados.backend.domain.model.entity.DetalleCuota;
import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Periodo;
import com.unicauca.pensionados.backend.domain.model.mappers.cuota.CuotaPagarMapper;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.CuotaPagarRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PeriodoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CuotaPagarServicio implements ICuotaPagarServicio {

    private final CuotaPagarRepositorio cuotaPagarRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final PeriodoRepositorio periodoRepositorio;

    public CuotaPagarServicio(CuotaPagarRepositorio cuotaPagarRepositorio,
                              PensionadoRepositorio pensionadoRepositorio,
                              EntidadRepositorio entidadRepositorio,
                              PeriodoRepositorio periodoRepositorio) {
        this.cuotaPagarRepositorio = cuotaPagarRepositorio;
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadRepositorio = entidadRepositorio;
        this.periodoRepositorio = periodoRepositorio;
    }

    @Transactional
    @Override
    public CuotaPagarRespuestaDTO crear(RegistroCuotaPagarPeticion peticion) {
        CuotaPagar cuota = new CuotaPagar();
        aplicar(cuota, peticion);
        cuota = cuotaPagarRepositorio.save(cuota);
        return CuotaPagarMapper.toDTO(cuota);
    }

    @Transactional
    @Override
    public CuotaPagarRespuestaDTO actualizar(Long id, RegistroCuotaPagarPeticion peticion) {
        CuotaPagar cuota = cuotaPagarRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la cuota por pagar con ID: " + id));
        aplicar(cuota, peticion);
        cuota = cuotaPagarRepositorio.save(cuota);
        return CuotaPagarMapper.toDTO(cuota);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        CuotaPagar cuota = cuotaPagarRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la cuota por pagar con ID: " + id));
        cuotaPagarRepositorio.delete(cuota);
    }

    @Override
    public CuotaPagarRespuestaDTO obtenerPorId(Long id) {
        CuotaPagar cuota = cuotaPagarRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la cuota por pagar con ID: " + id));
        return CuotaPagarMapper.toDTO(cuota);
    }

    @Override
    public List<CuotaPagarRespuestaDTO> listar() {
        return cuotaPagarRepositorio.findAll().stream().map(CuotaPagarMapper::toDTO).toList();
    }

    private void aplicar(CuotaPagar cuota, RegistroCuotaPagarPeticion p) {
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

        // ========== CÁLCULOS AUTOMÁTICOS ==========

        // 1. Calcular porcentaje y valor de cuota parte (si no viene en la petición)
        java.math.BigDecimal porcentajeCuotaParte;
        if (p.getValorCuotaParte() != null) {
            // Si el cliente proporciona el valor directamente, lo usamos
            cuota.setValorCuotaParte(p.getValorCuotaParte());
        } else {
            // Calcular desde los datos del pensionado
            // % Cuota Parte = (Días Entidad / Total Días) * 100
            porcentajeCuotaParte = calcularPorcentajeCuotaParte(
                pensionado.getDiasTrabajadosEntidad(),
                pensionado.getDiasTotalesTrabajados()
            );

            // Valor Cuota Parte = Valor Pensión * (% / 100)
            java.math.BigDecimal valorCuotaParte = calcularValorCuotaParte(
                pensionado.getValorPensionActual(),
                porcentajeCuotaParte
            );
            cuota.setValorCuotaParte(valorCuotaParte);
        }

        // 2. Calcular el total de cuotas (valor mensual * cantidad de meses)
        java.math.BigDecimal valorCuotasTotal = calcularValorCuotasTotal(
            cuota.getValorCuotaParte(),
            cuota.getCantidadCuotas()
        );
        cuota.setValorCuotasTotal(valorCuotasTotal);

        // 3. Calcular el total a pagar (cuotas + ajuste)
        // NO se calculan intereses en cuotas normales
        java.math.BigDecimal totalPagar = calcularTotalPagar(
            valorCuotasTotal,
            java.math.BigDecimal.ZERO,  // Sin intereses
            cuota.getAjuste()
        );
        cuota.setTotalPagar(totalPagar);

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
                d.setCuotaPagar(cuota);
                d.setCuotaCobrar(null);
                d.setDescripcion(det.getDescripcion());
                d.setValor(det.getValor());
                cuota.getDetallesCuotas().add(d);
            }
        }
    }

    // ==================== MÉTODOS DE CÁLCULO ====================

    /**
     * Calcula el porcentaje de cuota parte basado en días trabajados
     * Fórmula: % = (Días Entidad / Total Días) * 100
     * @param diasEntidad Días trabajados en la entidad específica
     * @param diasTotales Total de días trabajados del pensionado
     * @return Porcentaje de cuota parte
     */
    private java.math.BigDecimal calcularPorcentajeCuotaParte(Integer diasEntidad, Integer diasTotales) {
        if (diasEntidad == null || diasTotales == null || diasTotales == 0) {
            return java.math.BigDecimal.ZERO;
        }
        return java.math.BigDecimal.valueOf(diasEntidad)
                .divide(java.math.BigDecimal.valueOf(diasTotales), 6, java.math.RoundingMode.HALF_UP)
                .multiply(java.math.BigDecimal.valueOf(100))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Calcula el valor mensual de la cuota parte
     * Fórmula: Valor Cuota = Valor Pensión * (% Cuota Parte / 100)
     * @param valorPension Valor de la pensión mensual del pensionado
     * @param porcentajeCuotaParte Porcentaje que corresponde a la entidad
     * @return Valor mensual de la cuota parte
     */
    private java.math.BigDecimal calcularValorCuotaParte(java.math.BigDecimal valorPension, java.math.BigDecimal porcentajeCuotaParte) {
        if (valorPension == null || porcentajeCuotaParte == null) {
            return java.math.BigDecimal.ZERO;
        }
        return valorPension
                .multiply(porcentajeCuotaParte)
                .divide(java.math.BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Calcula el valor total de las cuotas para múltiples meses
     * Fórmula: Total = Valor Cuota Mensual * Cantidad de Meses
     * @param valorCuotaParte Valor mensual de la cuota parte
     * @param cantidadCuotas Número de meses/cuotas
     * @return Valor total de todas las cuotas
     */
    private java.math.BigDecimal calcularValorCuotasTotal(java.math.BigDecimal valorCuotaParte, Integer cantidadCuotas) {
        if (valorCuotaParte == null || cantidadCuotas == null) {
            return java.math.BigDecimal.ZERO;
        }
        return valorCuotaParte.multiply(java.math.BigDecimal.valueOf(cantidadCuotas))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Calcula el total a pagar (suma de cuotas + ajustes)
     * NOTA: Ya no se calculan intereses en cuotas normales.
     * @param valorCuotasTotal Total de las cuotas
     * @param valorInteresTotal Siempre será ZERO (mantenido por compatibilidad)
     * @param ajuste Ajuste manual (puede ser positivo o negativo)
     * @return Total a pagar
     */
    private java.math.BigDecimal calcularTotalPagar(
            java.math.BigDecimal valorCuotasTotal,
            java.math.BigDecimal valorInteresTotal,
            java.math.BigDecimal ajuste) {

        java.math.BigDecimal total = java.math.BigDecimal.ZERO;

        if (valorCuotasTotal != null) {
            total = total.add(valorCuotasTotal);
        }
        // valorInteresTotal siempre será 0, pero se mantiene para no romper la firma
        if (valorInteresTotal != null && valorInteresTotal.compareTo(java.math.BigDecimal.ZERO) > 0) {
            total = total.add(valorInteresTotal);
        }
        if (ajuste != null) {
            total = total.add(ajuste);
        }

        return total.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
