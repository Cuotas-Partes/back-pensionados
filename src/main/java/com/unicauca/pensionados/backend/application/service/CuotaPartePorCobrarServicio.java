package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.domain.model.entity.*;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuotaParte;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.CuotaPartePorCobrarRepository;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las cuotas partes por cobrar.
 * Contiene toda la lógica de negocio para cálculos y operaciones CRUD.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CuotaPartePorCobrarServicio {

    private final CuotaPartePorCobrarRepository cuotaPartePorCobrarRepository;
    private final PensionadoRepositorio pensionadoRepository;
    private final EntidadRepositorio entidadRepository;
    private final com.unicauca.pensionados.backend.infrastructure.persistence.repository.DTFRepositorio dtfRepositorio;
    private final com.unicauca.pensionados.backend.infrastructure.persistence.repository.IPCRepositorio ipcRepositorio;
    private final com.unicauca.pensionados.backend.infrastructure.persistence.repository.SMMLVHistoricoRepositorio smmlvRepositorio;

    /**
     * Crear una nueva cuota parte por cobrar
     */
    public CuotaPartePorCobrar crear(CuotaPartePorCobrar cuotaParte) {
        log.info("Creando nueva cuota parte por cobrar para pensionado: {}", cuotaParte.getCedulaPensionado());

        // Validar y cargar pensionado
        if (cuotaParte.getPensionado() != null && cuotaParte.getPensionado().getIdPersona() != null) {
            Pensionado pensionado = pensionadoRepository.findById(cuotaParte.getPensionado().getIdPersona())
                    .orElseThrow(() -> new RecursoNoEncontrado("Pensionado no encontrado"));
            cuotaParte.setPensionado(pensionado);
            cuotaParte.setCedulaPensionado(pensionado.getCedula());
            cuotaParte.setNombrePensionado(pensionado.getNombre() + " " + pensionado.getApellidos());
        }

        // Validar y cargar entidad deudora
        if (cuotaParte.getEntidadDeudora() != null && cuotaParte.getEntidadDeudora().getIdEntidad() != null) {
            Entidad entidad = entidadRepository.findById(cuotaParte.getEntidadDeudora().getIdEntidad())
                    .orElseThrow(() -> new RecursoNoEncontrado("Entidad deudora no encontrada"));
            cuotaParte.setEntidadDeudora(entidad);
            cuotaParte.setNitEntidadDeudora(entidad.getNit());
            cuotaParte.setNombreEntidadDeudora(entidad.getName());
        }

        // Construir descripción del periodo
        if (cuotaParte.getPeriodoDescripcion() == null) {
            cuotaParte.setPeriodoDescripcion(
                obtenerNombreMes(cuotaParte.getMesPeriodo()) + " " + cuotaParte.getAnioPeriodo()
            );
        }

        // Realizar cálculos completos
        calcularCuotaParteCompleta(cuotaParte);

        return cuotaPartePorCobrarRepository.save(cuotaParte);
    }

    /**
     * Buscar todas las cuotas partes por cobrar
     */
    @Transactional(readOnly = true)
    public List<CuotaPartePorCobrar> buscarTodas() {
        return cuotaPartePorCobrarRepository.findAll();
    }

    /**
     * Buscar cuota parte por ID
     */
    @Transactional(readOnly = true)
    public CuotaPartePorCobrar buscarPorId(Long id) {
        return cuotaPartePorCobrarRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Cuota parte por cobrar no encontrada con ID: " + id));
    }

    /**
     * Buscar cuotas partes por pensionado
     */
    @Transactional(readOnly = true)
    public List<CuotaPartePorCobrar> buscarPorPensionado(Long idPensionado) {
        return cuotaPartePorCobrarRepository.findByPensionado_IdPersona(idPensionado);
    }

    /**
     * Buscar cuotas partes por entidad deudora
     */
    @Transactional(readOnly = true)
    public List<CuotaPartePorCobrar> buscarPorEntidadDeudora(Long idEntidad) {
        return cuotaPartePorCobrarRepository.findByEntidadDeudora_IdEntidad(idEntidad);
    }

    /**
     * Buscar cuotas partes por periodo
     */
    @Transactional(readOnly = true)
    public List<CuotaPartePorCobrar> buscarPorPeriodo(Integer anio, Integer mes) {
        return cuotaPartePorCobrarRepository.findByAnioPeriodoAndMesPeriodo(anio, mes);
    }

    /**
     * Buscar cuotas partes por estado
     */
    @Transactional(readOnly = true)
    public List<CuotaPartePorCobrar> buscarPorEstado(EstadoCuotaParte estado) {
        return cuotaPartePorCobrarRepository.findByEstado(estado);
    }

    /**
     * Actualizar una cuota parte por cobrar
     */
    public CuotaPartePorCobrar actualizar(Long id, CuotaPartePorCobrar cuotaParteActualizada) {
        log.info("Actualizando cuota parte por cobrar con ID: {}", id);

        CuotaPartePorCobrar cuotaParteExistente = buscarPorId(id);

        // Actualizar campos básicos
        if (cuotaParteActualizada.getValorPensionBase() != null) {
            cuotaParteExistente.setValorPensionBase(cuotaParteActualizada.getValorPensionBase());
        }
        if (cuotaParteActualizada.getDiasTrabajadosEntidad() != null) {
            cuotaParteExistente.setDiasTrabajadosEntidad(cuotaParteActualizada.getDiasTrabajadosEntidad());
        }
        if (cuotaParteActualizada.getDiasTotalesTrabajados() != null) {
            cuotaParteExistente.setDiasTotalesTrabajados(cuotaParteActualizada.getDiasTotalesTrabajados());
        }
        if (cuotaParteActualizada.getMesadasPagadas() != null) {
            cuotaParteExistente.setMesadasPagadas(cuotaParteActualizada.getMesadasPagadas());
        }
        if (cuotaParteActualizada.getEstado() != null) {
            cuotaParteExistente.setEstado(cuotaParteActualizada.getEstado());
        }
        if (cuotaParteActualizada.getObservaciones() != null) {
            cuotaParteExistente.setObservaciones(cuotaParteActualizada.getObservaciones());
        }

        // Recalcular valores
        calcularCuotaParteCompleta(cuotaParteExistente);

        return cuotaPartePorCobrarRepository.save(cuotaParteExistente);
    }

    /**
     * Marcar cuota parte como pagada
     */
    public CuotaPartePorCobrar marcarComoPagada(Long id, LocalDate fechaPago) {
        log.info("Marcando cuota parte {} como pagada", id);

        CuotaPartePorCobrar cuotaParte = buscarPorId(id);
        cuotaParte.setEstado(EstadoCuotaParte.PAGADA);
        cuotaParte.setFechaPago(fechaPago != null ? fechaPago : LocalDate.now());

        return cuotaPartePorCobrarRepository.save(cuotaParte);
    }

    /**
     * Eliminar una cuota parte por cobrar
     */
    public void eliminar(Long id) {
        log.info("Eliminando cuota parte por cobrar con ID: {}", id);

        if (!cuotaPartePorCobrarRepository.existsById(id)) {
            throw new RecursoNoEncontrado("Cuota parte por cobrar no encontrada con ID: " + id);
        }

        cuotaPartePorCobrarRepository.deleteById(id);
    }

    // ==================== MÉTODOS DE CÁLCULO ====================

    /**
     * Calcular el porcentaje de cuota parte
     */
    private void calcularPorcentajeCuotaParte(CuotaPartePorCobrar cuotaParte) {
        if (cuotaParte.getDiasTrabajadosEntidad() != null &&
            cuotaParte.getDiasTotalesTrabajados() != null &&
            cuotaParte.getDiasTotalesTrabajados() > 0) {

            BigDecimal porcentaje = new BigDecimal(cuotaParte.getDiasTrabajadosEntidad())
                .divide(new BigDecimal(cuotaParte.getDiasTotalesTrabajados()), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);

            cuotaParte.setPorcentajeCuotaParte(porcentaje);
        }
    }

    /**
     * Aplicar ajuste por inflación (IPC o DTF)
     */
    private void aplicarAjustePorInflacion(CuotaPartePorCobrar cuotaParte) {
        if (cuotaParte.getValorPensionBase() == null) {
            return;
        }

        BigDecimal valorAjustado = cuotaParte.getValorPensionBase();
        StringBuilder detalle = new StringBuilder();
        detalle.append("Cálculo de Cuota Parte - Periodo: ").append(cuotaParte.getPeriodoDescripcion()).append("\n");
        detalle.append("Valor Base: $").append(cuotaParte.getValorPensionBase()).append("\n");

        // Intentar obtener y aplicar IPC
        Optional<IPC> ipcOpt = ipcRepositorio.findByYear(cuotaParte.getAnioPeriodo());
        if (ipcOpt.isPresent()) {
            IPC ipc = ipcOpt.get();
            cuotaParte.setIpcAplicado(ipc);
            cuotaParte.setValorIpcUtilizado(ipc.getIpc());

            BigDecimal factorIpc = BigDecimal.ONE.add(
                ipc.getIpc().divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)
            );
            valorAjustado = cuotaParte.getValorPensionBase().multiply(factorIpc).setScale(2, RoundingMode.HALF_UP);
            cuotaParte.setFactorAjuste(factorIpc);

            detalle.append("IPC Aplicado: ").append(ipc.getIpc()).append("%\n");
            detalle.append("Factor de Ajuste IPC: ").append(factorIpc).append("\n");
        } else {
            // Si no hay IPC, intentar con DTF (buscar por periodo año completo, ej: "2020")
            log.debug("No se encontró IPC para el año {}, intentando con DTF", cuotaParte.getAnioPeriodo());

            // DTF usa periodo en formato String, buscar todos los que empiecen con el año
            List<DTF> dtfs = dtfRepositorio.findAll().stream()
                .filter(d -> d.getPeriodo() != null && d.getPeriodo().startsWith(cuotaParte.getAnioPeriodo().toString()))
                .toList();

            if (!dtfs.isEmpty()) {
                // Usar el primero encontrado o calcular promedio
                DTF dtf = dtfs.get(0);
                cuotaParte.setDtfAplicado(dtf);
                cuotaParte.setValorDtfUtilizado(dtf.getValor());

                BigDecimal factorDtf = BigDecimal.ONE.add(
                    dtf.getValor().divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)
                );
                valorAjustado = cuotaParte.getValorPensionBase().multiply(factorDtf).setScale(2, RoundingMode.HALF_UP);
                cuotaParte.setFactorAjuste(factorDtf);

                detalle.append("DTF Aplicado: ").append(dtf.getValor()).append("%\n");
                detalle.append("Factor de Ajuste DTF: ").append(factorDtf).append("\n");
            } else {
                log.debug("No se encontró DTF para el año {}", cuotaParte.getAnioPeriodo());
                detalle.append("Sin ajuste por inflación\n");
                cuotaParte.setFactorAjuste(BigDecimal.ONE);
            }
        }

        cuotaParte.setValorPensionAjustado(valorAjustado);
        detalle.append("Valor Ajustado: $").append(valorAjustado).append("\n");

        // Aplicar SMMLV si es necesario
        Optional<SMMLVHistorico> smmlvOpt = smmlvRepositorio.findByAno(cuotaParte.getAnioPeriodo());
        if (smmlvOpt.isPresent()) {
            SMMLVHistorico smmlv = smmlvOpt.get();
            cuotaParte.setSmmlvAplicado(smmlv);
            cuotaParte.setValorSmmlvUtilizado(smmlv.getValor());

            if (valorAjustado.compareTo(smmlv.getValor()) < 0) {
                valorAjustado = smmlv.getValor();
                detalle.append("Ajustado a SMMLV: $").append(smmlv.getValor()).append("\n");
            }
        } else {
            log.debug("No se encontró SMMLV para el año {}", cuotaParte.getAnioPeriodo());
        }

        cuotaParte.setValorPensionPeriodo(valorAjustado);
        detalle.append("Valor Final Periodo: $").append(cuotaParte.getValorPensionPeriodo()).append("\n");
        cuotaParte.setDetalleCalculo(detalle.toString());
    }

    /**
     * Calcular el valor de la cuota parte
     */
    private void calcularValorCuotaParte(CuotaPartePorCobrar cuotaParte) {
        if (cuotaParte.getValorPensionPeriodo() != null && cuotaParte.getPorcentajeCuotaParte() != null) {
            BigDecimal valorCuota = cuotaParte.getValorPensionPeriodo()
                .multiply(cuotaParte.getPorcentajeCuotaParte())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            // Si hay mesadas pagadas, multiplicar
            if (cuotaParte.getMesadasPagadas() != null && cuotaParte.getMesadasPagadas() > 0) {
                valorCuota = valorCuota.multiply(new BigDecimal(cuotaParte.getMesadasPagadas()));
            }

            cuotaParte.setValorCuotaParte(valorCuota);

            // Agregar al detalle de cálculo
            String detalleActual = cuotaParte.getDetalleCalculo() != null ? cuotaParte.getDetalleCalculo() : "";
            StringBuilder detalle = new StringBuilder(detalleActual);
            detalle.append("\nPorcentaje Cuota Parte: ").append(cuotaParte.getPorcentajeCuotaParte()).append("%\n");
            detalle.append("Días Trabajados Entidad: ").append(cuotaParte.getDiasTrabajadosEntidad()).append("\n");
            detalle.append("Días Totales: ").append(cuotaParte.getDiasTotalesTrabajados()).append("\n");
            if (cuotaParte.getMesadasPagadas() != null && cuotaParte.getMesadasPagadas() > 0) {
                detalle.append("Mesadas Pagadas: ").append(cuotaParte.getMesadasPagadas()).append("\n");
            }
            detalle.append("Valor Cuota Parte: $").append(cuotaParte.getValorCuotaParte());

            cuotaParte.setDetalleCalculo(detalle.toString());
        }
    }

    /**
     * Ejecutar todos los cálculos en orden
     */
    private void calcularCuotaParteCompleta(CuotaPartePorCobrar cuotaParte) {
        calcularPorcentajeCuotaParte(cuotaParte);
        aplicarAjustePorInflacion(cuotaParte);
        calcularValorCuotaParte(cuotaParte);
    }

    /**
     * Obtener nombre del mes
     */
    private String obtenerNombreMes(Integer mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return (mes != null && mes >= 1 && mes <= 12) ? meses[mes - 1] : "Mes " + mes;
    }
}

