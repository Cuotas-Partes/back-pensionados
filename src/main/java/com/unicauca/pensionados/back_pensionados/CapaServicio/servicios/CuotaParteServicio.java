package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaParteRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PensionadoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.FiltroCuotaPartePeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.CuotaParteDTO;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadValorCuotaParteDTO;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoConCuotaParteDTO;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPensionado;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPeriodoDTO;
import com.unicauca.pensionados.back_pensionados.config.EntidadProperties;

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
            BigDecimal totalDiasTrabajo = BigDecimal.valueOf(trabajo.getPensionado().getTotalDiasTrabajo());
            if (totalDiasTrabajo.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Total de dias de trabajo no puede ser cero");
            }
            BigDecimal porcentajeCuotaParte = diasDeServicio.divide(totalDiasTrabajo, 4, RoundingMode.HALF_UP);
            MonetaryAmount valorInicialPension = Money.of(trabajo.getPensionado().getValorInicialPension(), Monetary.getCurrency("COP"));
            MonetaryAmount valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);cuotaParte.setTrabajo(trabajo);
            cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
            cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
            cuotaParte.setFechaGeneracion(LocalDate.now());
            cuotaParte.setNotas(porcentajeCuotaParte.toString());
            cuotaParteRepositorio.save(cuotaParte);
            periodoRepositorio.deleteByCuotaParte_IdCuotaParte(cuotaParte.getIdCuotaParte());


            LocalDate fechaInicioPensionDate = trabajo.getPensionado().getFechaInicioPension();
            LocalDate fechaInicioPension =  fechaInicioPensionDate;

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
    /**
    * Elimina físicamente una {@link CuotaParte} asociada a un {@link Trabajo}.
    * Este método elimina permanentemente la información 
    * Debe añadir se validaciones adicionales y añadir histórico.
    * Confirmar si tiene periodos asociados.
    * Validar si la cuota parte ya está liquidada o pagada.
    * Registrar motivo o usuario que ejecutó la eliminación
    * verificar si el Trabajo está vinculado a otros cálculos dependientes 
        (ej. recalculo de total días, valor de pensión, históricos).
    * validar si el Trabajo pasado como parámetro es nulo o no existe.
    * se debe marcar como eliminación lógica (soft delete)
    * no permitir eliminar si hay calculos en ejecucion
    * mejorar que no se realice borrado físico del registro.
    *
    * @param trabajo Trabajo asociado a la cuota parte a eliminar.
    */
    public void eliminarCuotaPartePorIdTrabajo(Trabajo trabajo) {
        CuotaParte cuotaParte = buscarPorTrabajoId(trabajo.getIdTrabajo());
        cuotaParteRepositorio.delete(cuotaParte);
    }
    
    /**
    * Actualiza  la información de una {@link CuotaParte} asociada a un {@link Trabajo}.
    * Este método realiza el cálculo proporcional de la cuota parte con base en los días de servicio
    * del trabajo y el total de días laborados por el pensionado. También actualiza el valor monetario
    * correspondiente al porcentaje calculado y genera nuevamente los periodos de servicio asociados.
    * Flujo principal:
    * Obtiene la cuota parte vinculada al trabajo (si existe).
    * Calcula el porcentaje de cuota parte: {@code diasDeServicio / totalDiasTrabajo}.
    * Calcula el valor monetario proporcional sobre el valor inicial de la pensión.
    * Actualiza los campos de la cuota parte (valor, porcentaje, fecha, notas).
    * Guarda los cambios y regenera los periodos asociados.
    * {@link ArithmeticException} si el total de días de trabajo del pensionado es cero.
    * {@link NullPointerException} si alguno de los objetos obligatorios es nulo (Trabajo, Pensionado, etc.).
 * PENDIENTE:
 *   Agregar validaciones de si {@link CuotaParte} ya existe en base de datos antes de recalcular.
 *   Controlar que el valor de días de servicio del trabajo no sea negativo o inconsistente.
 *   Verificar el valor inicial de la pensión para que sea válido (ej. null o negativo).
 *   No existe manejo explícito de transacciones anidadas o rollback en caso de error al generar periodos.
 *   El método elimina todos los periodos asociados a la cuota parte sin verificar si hay registros históricos que no deberían borrarse.
 *   Sería recomendable separar la lógica de negocio (cálculo) de la lógica de persistencia (save/delete).
 *   Añadir Guardado en la tabla de Log de cuando se realizar la actualizacion de Cuota parte
 * @param trabajo El trabajo asociado al pensionado sobre el cual se debe calcular la cuota parte.
 * @throws ArithmeticException Si el total de días de trabajo es cero.
 * @throws NullPointerException Si el trabajo o su pensionado son nulos.
 */
    @Transactional
    @Override
    public void actualizarCuotaParte(Trabajo trabajo) {
        if (trabajo != null) {
            CuotaParte cuotaParte = buscarPorTrabajoId(trabajo.getIdTrabajo());
            BigDecimal diasDeServicio = BigDecimal.valueOf(trabajo.getDiasDeServicio());
            BigDecimal totalDiasTrabajo = BigDecimal.valueOf(trabajo.getPensionado().getTotalDiasTrabajo());
    
            if (totalDiasTrabajo.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Total de dias de trabajo no puede ser cero");
            }
    
            BigDecimal porcentajeCuotaParte = diasDeServicio.divide(totalDiasTrabajo, 4, RoundingMode.HALF_UP);
            MonetaryAmount valorInicialPension = Money.of(trabajo.getPensionado().getValorInicialPension(), Monetary.getCurrency("COP"));
            MonetaryAmount valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);
    
            cuotaParte.setTrabajo(trabajo);
            cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
            cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
            cuotaParte.setFechaGeneracion(LocalDate.now());
            cuotaParte.setNotas(porcentajeCuotaParte.toString());
            cuotaParteRepositorio.save(cuotaParte);
            periodoRepositorio.deleteByCuotaParte_IdCuotaParte(cuotaParte.getIdCuotaParte());

            LocalDate fechaInicioPensionDate = trabajo.getPensionado().getFechaInicioPension();
            LocalDate fechaInicioPension = fechaInicioPensionDate;

            periodoServicio.generarYCalcularPeriodos(fechaInicioPension, cuotaParte);
        }
    }
    

    /**
    * Recalcula las cuotas partes asociadas a un pensionado.
    *
    * Este método obtiene todos los trabajos registrados del pensionado y 
    * llama a {@link #actualizarCuotaParte(Trabajo)} para cada uno. 
    *PENDIENTE Se deben realizar validaciones previas sobre la existencia de cuotas partes 
    * anteriores, debe eliminar registros previos, para evitar generar 
    * duplicidades o inconsistencias si se invoca de manera incorrecta 
    * (por ejemplo, durante una importación parcial o antes de consolidar los trabajos).
    * @param pensionado el pensionado cuyas cuotas partes se deben recalcular
 */
    @Transactional
    @Override
    public void recalcularCuotasPartesPorPensionado(Pensionado pensionado){
        List<Trabajo> trabajos = trabajoRepositorio.findByPensionado(pensionado);
        for(Trabajo trabajo : trabajos){
            actualizarCuotaParte(trabajo);
        }
    }


    /**
    * Obtiene el listado de cuotas partes pendientes por cobrar agrupadas por pensionado
    * para la entidad configurada (Universidad del Cauca).
    *
    * Este método recupera todas las cuotas parte registradas en el sistema y aplica los
    * siguientes filtros:
    * Solo se incluyen pensionados cuya entidad de jubilación coincide con la entidad configurada en {@code entidadProperties}.
    * Se excluyen las cuotas parte originadas por la misma entidad emisora (evita el autocobro, las de nit {@code 8911500319L}).
    * Para cada pensionado válido, se agrupan las cuotas parte asociadas, sumando el
    * total de valores registrados en los periodos ({@code CuotaParteTotalAnio}) y construyendo
    * un resumen del valor total a cobrar.
    * El resultado final consolida la información de todos los pensionados de la entidad,
    * indicando el detalle de sus cuotas parte y el total global de cobros pendientes.
    * @return un objeto {@link ResultadoCobroPorPensionado} que contiene:
    *Una lista de {@link PensionadoConCuotaParteDTO} con el detalle de cuotas parte por pensionado.
    *El valor total general de cobros pendientes por cuotas parte.
    *
    * @see CuotaParte
    * @see Pensionado
    * @see PensionadoConCuotaParteDTO
    * @see ResultadoCobroPorPensionado
 */

    @Override
    public ResultadoCobroPorPensionado  cuotasPartesPorCobrarPensionado() {
        List<CuotaParte> todasLasCuotasParte = cuotaParteRepositorio.findAll();
    
        Map<Long, PensionadoConCuotaParteDTO> mapPensionados = new HashMap<>();
    
        for (CuotaParte cuota : todasLasCuotasParte) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();
    
            if (pensionado == null
                || pensionado.getEntidadJubilacion() == null

                || !entidadProperties.getNombre().equalsIgnoreCase(pensionado.getEntidadJubilacion().getNombreEntidad())) {

                // Ignorar pensionados que no sean de UniCauca
                continue;
            }
    
            // revisamos la entidad de la CUOTA PARTE para omitir las cuotas de entidad con nit 8911500319L que pertenecen a unicauca
        
            Long nitEntidadCuota = cuota.getTrabajo().getEntidad().getNitEntidad();

            if (nitEntidadCuota != null && nitEntidadCuota.equals(entidadProperties.getNit())) {

                // Omitir cuota parte que pertenece a unicauca
                continue;
            }
    
            Long idPensionado = pensionado.getIdPersona();
    
            PensionadoConCuotaParteDTO dto = mapPensionados.computeIfAbsent(idPensionado, k ->
                new PensionadoConCuotaParteDTO(
                    pensionado.getTipoIdentificacion().name(), // Se añade el tipo de ID
                    pensionado.getNumeroIdentificacion(),      // Se usa el nuevo getter
                    pensionado.getNombrePersona(),
                    pensionado.getApellidosPersona(),
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
    /**
     * Metodo similar a "listarEntidadesYValorPorPensionadoYRango" solo que agrupa por pensionado
     * Calcula el valor total de las cuotas partes cobrables por un período específico
     * (año y rango de meses) para todos los pensionados asociados a la entidad actual.
     * El método obtiene todas las cuotas parte registradas, filtra aquellas que:
     * Pertenecen a pensionados cuya entidad de jubilación coincide con la entidad configurada en {@code entidadProperties}
     * No pertenecen a la misma entidad emisora (evita la doble contabilización)
     * Tienen periodos asociados al año indicado en el filtro.
     * Para cada cuota parte válida, el método:
     * Determina el número de mesadas a cobrar según el rango de meses definido en el filtro.
     * Calcula el valor total anual multiplicando la cuota mensual por el número de mesadas.
     * Agrupa los resultados por pensionado, acumulando el total de cobro por año.
     * El resultado final incluye una lista de pensionados con sus respectivas cuotas parte
     * y el total general consolidado del cobro.
     *  * @param filtro objeto {@link FiltroCuotaPartePeticion} que define los criterios de filtrado:
     *                 {@code anio}: año a evaluar (obligatorio)
     *                 {@code mesInicial}: mes de inicio del período (opcional, por defecto 1)
     *                 {@code mesFinal}: mes final del período (opcional, por defecto 12)
     * @return un objeto {@link ResultadoCobroPorPeriodoDTO} que contiene:
     *           Una lista de {@link PensionadoConCuotaParteDTO} con el detalle de cobro por pensionado.
     * 
     *          El total general de cobro de todas las cuotas parte en el período.
     * PENDIENTE: optimizar las consultas
     *  
     * */
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

                || !entidadProperties.getNombre().equalsIgnoreCase(pensionado.getEntidadJubilacion().getNombreEntidad())) {

                continue;
            }
    
            Long nitEntidadCuota = cuota.getTrabajo().getEntidad().getNitEntidad();

            if (nitEntidadCuota != null && nitEntidadCuota.equals(entidadProperties.getNit())) {

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
                    pensionado.getTipoIdentificacion().name(), // Se añade el tipo de ID
                    pensionado.getNumeroIdentificacion(),      // Se usa el nuevo getter
                    pensionado.getNombrePersona(),
                    pensionado.getApellidosPersona(),
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


   /**
    * PENDIENTE: Optimizar la consulta y hacer pruebas, ya que aunque conceptualmente realiza lo que se reqioere
     * para grandes volumnes de datos, puede ser ineficiente lo que puede generar que el servicio no responda
     * Este metodo Se encarga de: Lista las entidades donde trabajó un pensionado (de Unicauca) en un intervalo de tiempo,
     * mostrando el valor total de la cuota parte por cada entidad en ese periodo.
     * Devuelve una lista de EntidadValorCuotaParteDTO (DTOs) con NIT, nombre y valor a cobrar.
     */
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

        Map<Long, EntidadValorCuotaParteDTO> entidadValorMap = new HashMap<>();

        for (CuotaParte cuota : cuotasParte) {
            Pensionado pensionado = cuota.getTrabajo().getPensionado();

            if (pensionado == null || !idPensionado.equals(pensionado.getIdPersona())
                    || pensionado.getEntidadJubilacion() == null

                    || !entidadProperties.getNombre().equalsIgnoreCase(pensionado.getEntidadJubilacion().getNombreEntidad())) {

                continue;
            }

            Long nitEntidad = cuota.getTrabajo().getEntidad().getNitEntidad();

            if (nitEntidad != null && nitEntidad.equals(entidadProperties.getNit())) {

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
                    String.valueOf(nitEntidad),
                    cuota.getTrabajo().getEntidad().getNombreEntidad(),
                    BigDecimal.ZERO
                )
            );

            dto.setValorACobrar(dto.getValorACobrar().add(totalPorEntidad));
        }

        return new ArrayList<>(entidadValorMap.values());
    }

}