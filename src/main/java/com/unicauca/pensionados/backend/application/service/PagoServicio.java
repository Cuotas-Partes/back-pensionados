package com.unicauca.pensionados.backend.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.application.service.IPagoServicio;
import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pago;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.IPCRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PagoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.FiltroPagoPeticion;
import com.unicauca.pensionados.backend.application.dto.request.PagoDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.PagoDTORespuesta;
import com.unicauca.pensionados.backend.application.dto.response.ResumenPagoDTORespuesta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagoServicio implements IPagoServicio {

    private final PagoRepositorio pagoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final IPCRepositorio ipcRepositorio;

    @Override
    @Transactional
    public PagoDTORespuesta crear(PagoDTOPeticion peticion) {
        // Validar entidad
        Entidad entidad = entidadRepositorio.findByNitEntidad(peticion.getNitEntidad())
                .orElseThrow(() -> new RecursoNoEncontrado("Entidad no encontrada con NIT: " + peticion.getNitEntidad()));

        // Validar pensionado si se proporciona
        Pensionado pensionado = null;
        if (peticion.getIdPensionado() != null) {
            pensionado = pensionadoRepositorio.findById(peticion.getIdPensionado())
                    .orElseThrow(() -> new RecursoNoEncontrado("Pensionado no encontrado con ID: " + peticion.getIdPensionado()));
        }

        // Validar que el valor sea positivo
        if (peticion.getValorPagado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor pagado debe ser mayor que cero");
        }

        if (peticion.getCapital().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El capital debe ser mayor que cero");
        }

        // Validar que no exista un pago duplicado (misma entidad, mismo año, misma fecha)
        if (pagoRepositorio.existsByEntidadNitEntidadAndAnioAndFechaPago(
                peticion.getNitEntidad(), 
                peticion.getAnio(), 
                peticion.getFechaPago())) {
            throw new IllegalArgumentException(
                "Ya existe un pago registrado para esta entidad en el año " + 
                peticion.getAnio() + " con fecha " + peticion.getFechaPago());
        }

        // Crear el pago
        Pago pago = new Pago();
        pago.setEntidad(entidad);
        pago.setPensionado(pensionado);
        pago.setAnio(peticion.getAnio());
        pago.setValorPagado(peticion.getValorPagado());
        pago.setCapital(peticion.getCapital());
        pago.setFechaPago(peticion.getFechaPago());
        pago.setVerificado(peticion.getVerificado() != null ? peticion.getVerificado() : false);
        pago.setTipoPago(peticion.getTipoPago());
        pago.setObservaciones(peticion.getObservaciones());
        pago.setCreatedAt(LocalDateTime.now());

        // Calcular saldo
        pago.setSaldo(peticion.getCapital().subtract(peticion.getValorPagado()));

        // Obtener y configurar IPCs si se proporcionan (opcionales)
        if (peticion.getIpcInicialFecha() != null) {
            ipcRepositorio.findByFechaIPC(peticion.getIpcInicialFecha())
                    .ifPresent(ipcInicial -> {
                        pago.setIpcInicial(ipcInicial);
                        pago.setValorIpcInicial(ipcInicial.getValorIPC());
                    });
        }

        if (peticion.getIpcFinalFecha() != null) {
            ipcRepositorio.findByFechaIPC(peticion.getIpcFinalFecha())
                    .ifPresent(ipcFinal -> {
                        pago.setIpcFinal(ipcFinal);
                        pago.setValorIpcFinal(ipcFinal.getValorIPC());
                    });
        }

        // Calcular indexación si se tienen ambos IPCs
        if (pago.getValorIpcInicial() != null && pago.getValorIpcFinal() != null 
                && pago.getValorIpcInicial().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal factorIndexacion = pago.getValorIpcFinal()
                    .divide(pago.getValorIpcInicial(), 10, RoundingMode.HALF_UP);
            pago.setIndexacion(pago.getSaldo().multiply(factorIndexacion)
                    .setScale(2, RoundingMode.HALF_UP));
        } else {
            pago.setIndexacion(BigDecimal.ZERO);
        }

        // Calcular saldo pendiente
        pago.setSaldoPendiente(pago.getSaldo().add(pago.getIndexacion()));

        Pago pagoGuardado = pagoRepositorio.save(pago);
        return mapearARespuesta(pagoGuardado);
    }

    @Override
    @Transactional
    public PagoDTORespuesta actualizar(Long id, PagoDTOPeticion peticion) {
        Pago pago = pagoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Pago no encontrado con ID: " + id));

        // Validar entidad
        Entidad entidad = entidadRepositorio.findByNitEntidad(peticion.getNitEntidad())
                .orElseThrow(() -> new RecursoNoEncontrado("Entidad no encontrada con NIT: " + peticion.getNitEntidad()));

        // Validar pensionado si se proporciona
        Pensionado pensionado = null;
        if (peticion.getIdPensionado() != null) {
            pensionado = pensionadoRepositorio.findById(peticion.getIdPensionado())
                    .orElseThrow(() -> new RecursoNoEncontrado("Pensionado no encontrado con ID: " + peticion.getIdPensionado()));
        }

        // Validar que el valor sea positivo
        if (peticion.getValorPagado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor pagado debe ser mayor que cero");
        }

        if (peticion.getCapital().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El capital debe ser mayor que cero");
        }

        // Validar duplicados (excluyendo el pago actual)
        if (!pago.getEntidad().getNitEntidad().equals(peticion.getNitEntidad()) 
                || !pago.getAnio().equals(peticion.getAnio())
                || !pago.getFechaPago().equals(peticion.getFechaPago())) {
            if (pagoRepositorio.existsByEntidadNitEntidadAndAnioAndFechaPago(
                    peticion.getNitEntidad(), 
                    peticion.getAnio(), 
                    peticion.getFechaPago())) {
                throw new IllegalArgumentException(
                    "Ya existe otro pago registrado para esta entidad en el año " + 
                    peticion.getAnio() + " con fecha " + peticion.getFechaPago());
            }
        }

        // Actualizar campos
        pago.setEntidad(entidad);
        pago.setPensionado(pensionado);
        pago.setAnio(peticion.getAnio());
        pago.setValorPagado(peticion.getValorPagado());
        pago.setCapital(peticion.getCapital());
        pago.setFechaPago(peticion.getFechaPago());
        pago.setVerificado(peticion.getVerificado() != null ? peticion.getVerificado() : false);
        pago.setTipoPago(peticion.getTipoPago());
        pago.setObservaciones(peticion.getObservaciones());
        pago.setUpdatedAt(LocalDateTime.now());

        // Recalcular saldo
        pago.setSaldo(peticion.getCapital().subtract(peticion.getValorPagado()));

        // Actualizar IPCs si se proporcionan (opcionales)
        if (peticion.getIpcInicialFecha() != null) {
            ipcRepositorio.findByFechaIPC(peticion.getIpcInicialFecha())
                    .ifPresent(ipcInicial -> {
                        pago.setIpcInicial(ipcInicial);
                        pago.setValorIpcInicial(ipcInicial.getValorIPC());
                    });
        } else {
            pago.setIpcInicial(null);
            pago.setValorIpcInicial(null);
        }

        if (peticion.getIpcFinalFecha() != null) {
            ipcRepositorio.findByFechaIPC(peticion.getIpcFinalFecha())
                    .ifPresent(ipcFinal -> {
                        pago.setIpcFinal(ipcFinal);
                        pago.setValorIpcFinal(ipcFinal.getValorIPC());
                    });
        } else {
            pago.setIpcFinal(null);
            pago.setValorIpcFinal(null);
        }

        // Recalcular indexación
        if (pago.getValorIpcInicial() != null && pago.getValorIpcFinal() != null 
                && pago.getValorIpcInicial().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal factorIndexacion = pago.getValorIpcFinal()
                    .divide(pago.getValorIpcInicial(), 10, RoundingMode.HALF_UP);
            pago.setIndexacion(pago.getSaldo().multiply(factorIndexacion)
                    .setScale(2, RoundingMode.HALF_UP));
        } else {
            pago.setIndexacion(BigDecimal.ZERO);
        }

        // Recalcular saldo pendiente
        pago.setSaldoPendiente(pago.getSaldo().add(pago.getIndexacion()));

        Pago pagoGuardado = pagoRepositorio.save(pago);
        return mapearARespuesta(pagoGuardado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!pagoRepositorio.existsById(id)) {
            throw new RecursoNoEncontrado("Pago no encontrado con ID: " + id);
        }
        pagoRepositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoDTORespuesta obtenerPorId(Long id) {
        Pago pago = pagoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Pago no encontrado con ID: " + id));
        return mapearARespuesta(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTORespuesta> obtenerTodos() {
        return pagoRepositorio.findAll().stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTORespuesta> buscarConFiltros(FiltroPagoPeticion filtro) {
        List<Pago> pagos = pagoRepositorio.buscarConFiltros(
                filtro.getNitEntidad(),
                filtro.getAnioDesde(),
                filtro.getAnioHasta(),
                filtro.getFechaDesde(),
                filtro.getFechaHasta(),
                filtro.getVerificado(),
                filtro.getTipoPago(),
                filtro.getIdPensionado()
        );
        return pagos.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTORespuesta> obtenerPorEntidad(Long nitEntidad) {
        return pagoRepositorio.findByEntidadNitEntidad(nitEntidad).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTORespuesta> obtenerPorAnio(Integer anio) {
        return pagoRepositorio.findByAnio(anio).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTORespuesta> obtenerPorEntidadYAnio(Long nitEntidad, Integer anio) {
        return pagoRepositorio.findByEntidadNitEntidadAndAnio(nitEntidad, anio).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumenPagoDTORespuesta> obtenerResumenAgrupado(FiltroPagoPeticion filtro) {
        List<PagoDTORespuesta> pagos = buscarConFiltros(filtro);
        
        if (pagos.isEmpty()) {
            return List.of();
        }
        
        return pagos.stream()
                .collect(Collectors.groupingBy(
                        pago -> pago.getNitEntidad() + "-" + pago.getAnio(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                listaPagos -> {
                                    PagoDTORespuesta primerPago = listaPagos.get(0);
                                    ResumenPagoDTORespuesta resumen = new ResumenPagoDTORespuesta();
                                    resumen.setNitEntidad(primerPago.getNitEntidad());
                                    resumen.setNombreEntidad(primerPago.getNombreEntidad());
                                    resumen.setAnio(primerPago.getAnio());
                                    
                                    resumen.setTotalValorPagado(listaPagos.stream()
                                            .map(PagoDTORespuesta::getValorPagado)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                                    
                                    resumen.setTotalCapital(listaPagos.stream()
                                            .map(PagoDTORespuesta::getCapital)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                                    
                                    resumen.setTotalSaldo(listaPagos.stream()
                                            .map(PagoDTORespuesta::getSaldo)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                                    
                                    resumen.setTotalIndexacion(listaPagos.stream()
                                            .map(p -> p.getIndexacion() != null ? p.getIndexacion() : BigDecimal.ZERO)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                                    
                                    resumen.setTotalSaldoPendiente(listaPagos.stream()
                                            .map(p -> p.getSaldoPendiente() != null ? p.getSaldoPendiente() : BigDecimal.ZERO)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                                    
                                    resumen.setCantidadPagos(listaPagos.size());
                                    resumen.setCantidadPagosVerificados(
                                            (int) listaPagos.stream()
                                                    .filter(PagoDTORespuesta::getVerificado)
                                                    .count());
                                    resumen.setCantidadPagosPendientes(
                                            (int) listaPagos.stream()
                                                    .filter(p -> !p.getVerificado())
                                                    .count());
                                    
                                    return resumen;
                                }
                        )
                ))
                .values()
                .stream()
                .sorted((a, b) -> {
                    int cmp = a.getNitEntidad().compareTo(b.getNitEntidad());
                    if (cmp != 0) return cmp;
                    return b.getAnio().compareTo(a.getAnio());
                })
                .collect(Collectors.toList());
    }

    private PagoDTORespuesta mapearARespuesta(Pago pago) {
        PagoDTORespuesta respuesta = new PagoDTORespuesta();
        respuesta.setIdPago(pago.getIdPago());
        if (pago.getEntidad() != null) {
            respuesta.setNitEntidad(pago.getEntidad().getNitEntidad());
            respuesta.setNombreEntidad(pago.getEntidad().getNombreEntidad());
        }
        if (pago.getPensionado() != null) {
            respuesta.setIdPensionado(pago.getPensionado().getIdPersona());
            respuesta.setNombrePensionado(
                    pago.getPensionado().getNombrePersona() + " " + 
                    pago.getPensionado().getApellidosPersona());
        }
        respuesta.setAnio(pago.getAnio());
        respuesta.setValorPagado(pago.getValorPagado());
        respuesta.setCapital(pago.getCapital());
        respuesta.setSaldo(pago.getSaldo());
        if (pago.getIpcInicial() != null) {
            respuesta.setIpcInicialFecha(pago.getIpcInicial().getFechaIPC());
        }
        respuesta.setValorIpcInicial(pago.getValorIpcInicial());
        if (pago.getIpcFinal() != null) {
            respuesta.setIpcFinalFecha(pago.getIpcFinal().getFechaIPC());
        }
        respuesta.setValorIpcFinal(pago.getValorIpcFinal());
        respuesta.setIndexacion(pago.getIndexacion());
        respuesta.setSaldoPendiente(pago.getSaldoPendiente());
        respuesta.setFechaPago(pago.getFechaPago());
        respuesta.setVerificado(pago.getVerificado());
        respuesta.setTipoPago(pago.getTipoPago());
        respuesta.setObservaciones(pago.getObservaciones());
        respuesta.setCreatedAt(pago.getCreatedAt());
        respuesta.setUpdatedAt(pago.getUpdatedAt());
        return respuesta;
    }
}

