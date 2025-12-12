package com.unicauca.pensionados.backend.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import com.unicauca.pensionados.backend.application.service.interfaces.ICuotaParteServicio;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.backend.domain.model.entity.CuotaParte;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Periodo;
import com.unicauca.pensionados.backend.domain.model.entity.Trabajo;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.CuotaParteRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PeriodoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.TrabajoRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.FiltroCuotaPartePeticion;
import com.unicauca.pensionados.backend.application.dto.response.CuotaParteDTO;
import com.unicauca.pensionados.backend.application.dto.response.EntidadValorCuotaParteDTO;
import com.unicauca.pensionados.backend.application.dto.response.PensionadoConCuotaParteDTO;
import com.unicauca.pensionados.backend.application.dto.response.ResultadoCobroPorPensionado;
import com.unicauca.pensionados.backend.application.dto.response.ResultadoCobroPorPeriodoDTO;
import com.unicauca.pensionados.backend.infrastructure.config.EntidadProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import jakarta.transaction.Transactional;

@Service
public class CuotaParteServicio implements ICuotaParteServicio {
    private static final Logger logger = LoggerFactory.getLogger(CuotaParteServicio.class);


    private final CuotaParteRepositorio cuotaParteRepositorio;

    private final TrabajoRepositorio trabajoRepositorio;

    private final PeriodoServicio periodoServicio;
   
    private final PeriodoRepositorio periodoRepositorio;

    private final PensionadoRepositorio pensionadoRepositorio;


    private final EntidadProperties entidadProperties;

    public CuotaParteServicio (CuotaParteRepositorio cuotaParteRepositorio, TrabajoRepositorio trabajoRepositorio, PeriodoServicio periodoServicio, PeriodoRepositorio periodoRepositorio, PensionadoRepositorio pensionadoRepositorio, EntidadProperties entidadProperties){

        this.cuotaParteRepositorio = cuotaParteRepositorio;
        this.trabajoRepositorio = trabajoRepositorio;
        this.periodoServicio = periodoServicio;
        this.periodoRepositorio = periodoRepositorio;
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadProperties = entidadProperties;

    }

    @Transactional
    @Override
    public void registrarCuotaParte(Trabajo trabajo){

            if (trabajo != null) {
            // Buscar si ya existe una cuota parte para este trabajo
            CuotaParte cuotaParte = cuotaParteRepositorio.findByTrabajoIdTrabajo(trabajo.getIdTrabajo())
                .orElse(null);

            if (cuotaParte == null) {
                cuotaParte = new CuotaParte();
                cuotaParte.setTrabajo(trabajo);
            }
            BigDecimal diasDeServicio = BigDecimal.valueOf(trabajo.getDiasDeServicio());
            BigDecimal totalDiasTrabajo = BigDecimal.valueOf(trabajo.getPensionado().getDiasTotalesTrabajados());
            if (totalDiasTrabajo.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Total de dias de trabajo no puede ser cero");
            }
            BigDecimal porcentajeCuotaParte = diasDeServicio.divide(totalDiasTrabajo, 4, RoundingMode.HALF_UP);
            MonetaryAmount valorInicialPension = Money.of(trabajo.getPensionado().getValorPension(), Monetary.getCurrency("COP"));
            MonetaryAmount valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);cuotaParte.setTrabajo(trabajo);
            cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
            cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
            cuotaParte.setFechaGeneracion(LocalDate.now());
            cuotaParte.setNotas(porcentajeCuotaParte.toString());
            cuotaParteRepositorio.save(cuotaParte);
            periodoRepositorio.deleteByCuotaParte_IdCuotaParte(cuotaParte.getIdCuotaParte());

            // TODO: fechaInicioPension ya no está en Pensionado, necesita obtenerse de otra fuente
            LocalDate fechaInicioPension = LocalDate.now(); // Temporal

            periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);
            /* 
            MonetaryAmount valorInicialPension = Money.of(trabajo.getPensionado().getValorInicialPension(), Monetary.getCurrency("COP"));
            BigDecimal porcentajeCuotaParte = BigDecimal.valueOf(trabajo.getDiasDeServicio()/trabajo.getPensionado().getTotalDiasTrabajo());
            MonetaryAmount valorCuotaParteMoney=Money.of(0, Monetary.getCurrency("COP"));
            valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);
            cuotaParte.setTrabajo(trabajo);
            cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
            cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
            cuotaParte.setFechaGeneracion(LocalDate.now());
            cuotaParte.setNotas("Si funiono la parte de registro de cuota Parte");
            cuotaParteRepositorio.save(cuotaParte);
            cuotaParte.setTrabajo(trabajo);
            Date fechaInicioPensionDate = trabajo.getPensionado().getFechaInicioPension();
            LocalDate fechaInicioPension = ((java.sql.Date) fechaInicioPensionDate).toLocalDate();
            periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);*/
            
        }
        
    }

    public CuotaParte buscarPorTrabajoId(Long idTrabajo) {
        return cuotaParteRepositorio.findById(idTrabajo)
            .orElseThrow(() -> new RuntimeException("CuotaParte no encontrada para trabajo id: " + idTrabajo));
    }

    public void eliminarCuotaPartePorIdTrabajo(Trabajo trabajo) {
        CuotaParte cuotaParte = buscarPorTrabajoId(trabajo.getIdTrabajo());
        cuotaParteRepositorio.delete(cuotaParte);
    }
    
    @Transactional
    @Override
    public void actualizarCuotaParte(Trabajo trabajo) {
        if (trabajo != null) {
            CuotaParte cuotaParte = buscarPorTrabajoId(trabajo.getIdTrabajo());
            BigDecimal diasDeServicio = BigDecimal.valueOf(trabajo.getDiasDeServicio());
            BigDecimal totalDiasTrabajo = BigDecimal.valueOf(trabajo.getPensionado().getDiasTotalesTrabajados());
    
            if (totalDiasTrabajo.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Total de dias de trabajo no puede ser cero");
            }
    
            BigDecimal porcentajeCuotaParte = diasDeServicio.divide(totalDiasTrabajo, 4, RoundingMode.HALF_UP);
            MonetaryAmount valorInicialPension = Money.of(trabajo.getPensionado().getValorPension(), Monetary.getCurrency("COP"));
            MonetaryAmount valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);
    
            cuotaParte.setTrabajo(trabajo);
            cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
            cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
            cuotaParte.setFechaGeneracion(LocalDate.now());
            cuotaParte.setNotas(porcentajeCuotaParte.toString());

            cuotaParte.setFechaActualizacion(LocalDate.now());
            cuotaParte.setUsuarioActualizacion("SISTEMA"); // TODO: El usuario "SISTEMA" es temporal .Se debe reemplazar con el usuario autenticado del contexto de seguridad
            cuotaParte.setObservaciones("Registro de cuota parte actualizado por recálculo.");

            cuotaParteRepositorio.save(cuotaParte);
            periodoRepositorio.deleteByCuotaParte_IdCuotaParte(cuotaParte.getIdCuotaParte());

            // TODO: fechaInicioPension ya no está en Pensionado
            LocalDate fechaInicioPension = LocalDate.now(); // Temporal

            periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);
        }
    }
    

    @Transactional
    @Override
    public void recalcularCuotasPartesPorPensionado(Pensionado pensionado){
        List<Trabajo> trabajos = trabajoRepositorio.findByPensionado(pensionado);
        for(Trabajo trabajo : trabajos){
            actualizarCuotaParte(trabajo);
        }
    }



    @Override
    public ResultadoCobroPorPensionado  cuotasPartesPorCobrarPensionado() {
        List<CuotaParte> todasLasCuotasParte = cuotaParteRepositorio.findAll();
    
        Map<Long, PensionadoConCuotaParteDTO> mapPensionados = new HashMap<>();
    
        for (CuotaParte cuota : todasLasCuotasParte) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();
    
            if (pensionado == null
                || pensionado.getEntidadJubilacion() == null
                || !entidadProperties.getNombre().equalsIgnoreCase(pensionado.getEntidadJubilacion())) {
                // Ignorar pensionados que no sean de UniCauca
                continue;
            }
    
            // revisamos la entidad de la CUOTA PARTE para omitir las cuotas de entidad con nit 8911500319L que pertenecen a unicauca
        
            String nitEntidadCuota = cuota.getTrabajo().getEntidad().getNit();
            if (nitEntidadCuota != null && nitEntidadCuota.equals(String.valueOf(entidadProperties.getNit()))) {
                // Omitir cuota parte que pertenece a unicauca
                continue;
            }
    
            Long idPensionado = pensionado.getIdPersona();

            PensionadoConCuotaParteDTO dto = mapPensionados.computeIfAbsent(idPensionado, k ->
                new PensionadoConCuotaParteDTO(
                    "CC", // TODO: TipoIdentificacion ya no existe en Pensionado
                    Long.valueOf(pensionado.getCedula()),
                    pensionado.getNombre(),
                    pensionado.getApellidos(),
                    new ArrayList<>(),
                    BigDecimal.ZERO
                )
            );
            BigDecimal totalPeriodos = cuota.getPeriodos().stream()
            .map(Periodo::getCuotaParteTotalAnio) 
            .filter(Objects::nonNull)             
            .reduce(BigDecimal.ZERO, BigDecimal::add); 
            CuotaParteDTO cpDto = new CuotaParteDTO(
                cuota.getIdCuotaParte(),
                totalPeriodos,
                cuota.getFechaGeneracion()
            );

            dto.getCuotasParte().add(cpDto);
            

            // Sumar el valor de la cuota parte al total del pensionado
            dto.setValorTotalCobro(dto.getValorTotalCobro().add(totalPeriodos));
        }
    
        List<PensionadoConCuotaParteDTO> listaPensionados = new ArrayList<>(mapPensionados.values());
    
        BigDecimal totalGeneral = listaPensionados.stream()
            .map(PensionadoConCuotaParteDTO::getValorTotalCobro)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        return new ResultadoCobroPorPensionado(listaPensionados, totalGeneral);
    }

    public ResultadoCobroPorPeriodoDTO obtenerCobroPorPeriodo(FiltroCuotaPartePeticion filtro) {
        List<CuotaParte> todasLasCuotasParte = cuotaParteRepositorio.findAll();
        Map<Long, PensionadoConCuotaParteDTO> mapPensionados = new HashMap<>();
        int anio = filtro.getAnio();
        int mesInicio = 1;
        if(filtro.getMesInicial() != null){
            mesInicio = filtro.getMesInicial();
        }
        int mesFinal = 12;
        if(filtro.getMesFinal() != null){
            mesFinal = filtro.getMesFinal();
        }
        int mesada = calcularMesadas(mesInicio, mesFinal, anio);
        BigDecimal mesadaDecimal = BigDecimal.valueOf(mesada);
    
        for (CuotaParte cuota : todasLasCuotasParte) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();
    
            if (pensionado == null
                || pensionado.getEntidadJubilacion() == null
                || !entidadProperties.getNombre().equalsIgnoreCase(pensionado.getEntidadJubilacion())) {
                continue;
            }
    
            String nitEntidadCuota = cuota.getTrabajo().getEntidad().getNit();
            if (nitEntidadCuota != null && nitEntidadCuota.equals(String.valueOf(entidadProperties.getNit()))) {
                continue;
            }
    
            List<Periodo> periodos = cuota.getPeriodos();
            if (periodos == null || periodos.isEmpty()) {
                continue;
            }
    
            // Filtrar periodos por año y multiplicar cuotaParteMensual * numero de mesadas
            BigDecimal totalPorAnio = periodos.stream()
                .filter(p -> p.getFechaInicioPeriodo() != null && p.getFechaInicioPeriodo().getYear() == anio)
                .map(Periodo::getCuotaParteMensual)
                .filter(Objects::nonNull)
                .map(cuotaMensual -> cuotaMensual.multiply(mesadaDecimal))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    
            if (totalPorAnio.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
    
            Long idPensionado = pensionado.getIdPersona();
            PensionadoConCuotaParteDTO dto = mapPensionados.computeIfAbsent(idPensionado, k ->
                new PensionadoConCuotaParteDTO(
                    "CC", // TODO: TipoIdentificacion ya no existe en Pensionado
                    Long.parseLong(pensionado.getCedula()),
                    pensionado.getNombre(),
                    pensionado.getApellidos(),
                    new ArrayList<>(),
                    BigDecimal.ZERO
                )
            );
    
            CuotaParteDTO cpDto = new CuotaParteDTO(
                cuota.getIdCuotaParte(),
                totalPorAnio,
                cuota.getFechaGeneracion()
            );
    
            dto.getCuotasParte().add(cpDto);
            dto.setValorTotalCobro(dto.getValorTotalCobro().add(totalPorAnio));
        }
    
        List<PensionadoConCuotaParteDTO> listaPensionados = new ArrayList<>(mapPensionados.values());
    
        BigDecimal totalGeneral = listaPensionados.stream()
            .map(PensionadoConCuotaParteDTO::getValorTotalCobro)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        return new ResultadoCobroPorPeriodoDTO(listaPensionados, totalGeneral);
    }
    
    private int calcularMesadas(int mesInicio, int mesFinal, int anioPension) {
        if (mesInicio < 1 || mesInicio > 12) {
            throw new IllegalArgumentException("mesInicio debe estar entre 1 y 12");
        }
        if (mesFinal < 1 || mesFinal > 12) {
            throw new IllegalArgumentException("mesFinal debe estar entre 1 y 12");
        }
        if (mesInicio > mesFinal) {
            throw new IllegalArgumentException("mesInicio no puede ser mayor que mesFinal");
        }
    
        int mesadasOrdinarias = mesFinal - mesInicio + 1;
    
        int adicionales = 0;
        if (anioPension < 1993) {
            if (mesFinal >= 11 && mesInicio <= 11) {
                adicionales = 1;
            }
        } else {
            if (mesFinal >= 6 && mesInicio <= 6) adicionales++;
            if (mesFinal >= 11 && mesInicio <= 11) adicionales++;
        }
    
        return mesadasOrdinarias + adicionales;
    }



    @Transactional
    public List<EntidadValorCuotaParteDTO> listarEntidadesYValorPorPensionadoYRango(Long idPensionado, FiltroCuotaPartePeticion filtro) {
        logger.info("Inicio de listarEntidadesYValorPorPensionadoYRango - idPensionado: {}, filtro: {}", idPensionado, filtro);

        if (idPensionado == null || filtro == null || filtro.getAnio() == null) {
            throw new IllegalArgumentException("Todos los parámetros son obligatorios");
        }

        int anio = filtro.getAnio();
        int mesInicio = filtro.getMesInicial() != null ? filtro.getMesInicial() : 1;
        int mesFinal = filtro.getMesFinal() != null ? filtro.getMesFinal() : 12;

        if (mesInicio < 1 || mesInicio > 12 || mesFinal < 1 || mesFinal > 12 || mesInicio > mesFinal) {
            throw new IllegalArgumentException("Meses fuera de rango o mal definidos.");
        }

        int mesadas = calcularMesadas(mesInicio, mesFinal, anio);
        BigDecimal mesadasDecimal = BigDecimal.valueOf(mesadas);

        List<CuotaParte> cuotasParte = cuotaParteRepositorio.findAll(); // Reemplazar con query más específica si se desea

        Map<String, EntidadValorCuotaParteDTO> entidadValorMap = new HashMap<>();

        for (CuotaParte cuota : cuotasParte) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();

            if (pensionado == null || !idPensionado.equals(pensionado.getIdPersona())
                    || pensionado.getEntidadJubilacion() == null
                    || !entidadProperties.getNombre().equalsIgnoreCase(pensionado.getEntidadJubilacion())) {
                continue;
            }

            String nitEntidad = cuota.getTrabajo().getEntidad().getNit();
            if (nitEntidad != null && nitEntidad.equals(String.valueOf(entidadProperties.getNit()))) {
                continue;
            }

            List<Periodo> periodos = cuota.getPeriodos();
            if (periodos == null || periodos.isEmpty()) {
                continue;
            }

            BigDecimal totalPorEntidad = periodos.stream()
                .filter(p -> p.getFechaInicioPeriodo() != null && p.getFechaInicioPeriodo().getYear() == anio)
                .map(Periodo::getCuotaParteMensual)
                .filter(Objects::nonNull)
                .map(cuotaMensual -> cuotaMensual.multiply(mesadasDecimal))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalPorEntidad.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            EntidadValorCuotaParteDTO dto = entidadValorMap.computeIfAbsent(nitEntidad, k ->
                new EntidadValorCuotaParteDTO(
                    nitEntidad,
                    cuota.getTrabajo().getEntidad().getName(),
                    BigDecimal.ZERO
                )
            );

            dto.setValorACobrar(dto.getValorACobrar().add(totalPorEntidad));
        }

        return new ArrayList<>(entidadValorMap.values());
    }

}