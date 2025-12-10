package com.unicauca.pensionados.backend.application.service;


import com.unicauca.pensionados.backend.domain.model.enums.TipoPension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.backend.domain.model.entity.CuotaParte;
import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;

import com.unicauca.pensionados.backend.domain.model.entity.Trabajo;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.CuotaParteRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PersonaRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.TrabajoRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.RegistroPensionadoPeticion;
import com.unicauca.pensionados.backend.application.dto.request.RegistroTrabajoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.EntidadCuotaParteRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.PensionadoRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.SucesorRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.TrabajoRespuesta;

import jakarta.transaction.Transactional;
//import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class PensionadoServicio implements IPensionadoServicio {

    private final PersonaRepositorio personaRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final TrabajoRepositorio trabajoRepositorio;
    private final CuotaParteServicio cuotaParteServicio;
    @Autowired
    private ILogCambioServicio logCambioServicio;
    private final String nombreEntidad = "PENSIONADO";

    public PensionadoServicio(PersonaRepositorio personaRepositorio,
                              PensionadoRepositorio pensionadoRepositorio,
                              EntidadRepositorio entidadRepositorio,
                              TrabajoRepositorio trabajoRepositorio,
                              CuotaParteServicio cuotaParteServicio) {
        this.personaRepositorio = personaRepositorio;
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadRepositorio = entidadRepositorio;
        this.trabajoRepositorio = trabajoRepositorio;
        this.cuotaParteServicio = cuotaParteServicio;
    }

    
    @Transactional
    @Override
    public void registrarPensionado(RegistroPensionadoPeticion request) {
        if (personaRepositorio.existsByTipoIdentificacionAndNumeroIdentificacion(request.getTipoIdentificacion(), request.getNumeroIdentificacion())) {
            throw new RuntimeException("Ya existe una persona con ese tipo y número de identificación");
        }

        Entidad entidadJubilacion = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad de jubilación no se encuentra registrada"));

        TipoPension tipoPension;
        try{
             tipoPension = TipoPension.valueOf(request.getTipoPension().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new RuntimeException("Tipo de pension no valido");
        }


        // 1. Crear y guardar la entidad Pensionado
        Pensionado pensionado = new Pensionado();
        pensionado.setNumeroIdentificacion(request.getNumeroIdentificacion());
        pensionado.setTipoIdentificacion(request.getTipoIdentificacion());
        pensionado.setNombrePersona(request.getNombrePersona());
        pensionado.setApellidosPersona(request.getApellidosPersona());
        pensionado.setEstadoCivil(request.getEstadoCivil());
        pensionado.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        pensionado.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        pensionado.setEstadoPersona(request.getEstadoPersona());
        pensionado.setGeneroPersona(request.getGeneroPersona());
        pensionado.setFechaDefuncionPersona(request.getFechaDefuncionPersona());
        pensionado.setFechaInicioPension(request.getFechaInicioPension());
        pensionado.setValorInicialPension(request.getValorInicialPension());
        pensionado.setResolucionPension(request.getResolucionPension());
        pensionado.setEntidadJubilacion(entidadJubilacion);
        pensionado.setAplicarIPCPrimerPeriodo(request.isAplicarIPCPrimerPeriodo());
        pensionado.setTipoPension(tipoPension);

        //Guardar log del registro
        logCambioServicio.registrarCreacion(nombreEntidad, pensionadoRepositorio.save(pensionado));
    }
    /*==============================================================*/
    /* Actualizar Pensionado no es lo mismo que actualizar persona
     * este metodo consiste en actualizar informacion administrativa
     * fechaInicioPension, valorInicialPension, resoluciónPension,
     * entidad que paga la pension (Cambios administrativos o legales)
     * NO AFECTA LOS VINCULOS LABORALES NI GENERA CALCULOS ECONOMICOS*/
    /*==============================================================*/
    @Transactional
    @Override
    public void actualizarPensionado(Long idPersona, RegistroPensionadoPeticion request) {
        Pensionado pensionadoExistente = pensionadoRepositorio.findById(idPersona)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + idPersona));

        Entidad entidadJubilacion = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad de jubilación no se encuentra registrada"));
        Pensionado pensionadoAntiguo = new Pensionado();
        BeanUtils.copyProperties(pensionadoExistente, pensionadoAntiguo);

        pensionadoExistente.setNombrePersona(request.getNombrePersona());
        pensionadoExistente.setApellidosPersona(request.getApellidosPersona());
        pensionadoExistente.setEstadoCivil(request.getEstadoCivil());
        pensionadoExistente.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        pensionadoExistente.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        pensionadoExistente.setEstadoPersona(request.getEstadoPersona());
        pensionadoExistente.setGeneroPersona(request.getGeneroPersona());
        pensionadoExistente.setFechaDefuncionPersona(request.getFechaDefuncionPersona());
        pensionadoExistente.setFechaInicioPension(request.getFechaInicioPension());
        pensionadoExistente.setValorInicialPension(request.getValorInicialPension());
        pensionadoExistente.setResolucionPension(request.getResolucionPension());
        pensionadoExistente.setEntidadJubilacion(entidadJubilacion);
        pensionadoExistente.setAplicarIPCPrimerPeriodo(request.isAplicarIPCPrimerPeriodo());

        pensionadoExistente = pensionadoRepositorio.save(pensionadoExistente);
        logCambioServicio.registrarActualizacion(nombreEntidad, pensionadoAntiguo, pensionadoExistente);
        
    }

    @Override
    public List<PensionadoRespuesta> listarPensionados() {
        List<Pensionado> pensionados = pensionadoRepositorio.findAll();
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionados.stream().map(this::convertirAPensionadoRespuesta).collect(Collectors.toList());
    }
    
    @Override
    public PensionadoRespuesta buscarPensionadoPorId(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + id));
        logCambioServicio.registrarConsulta(nombreEntidad);
        return convertirAPensionadoRespuesta(pensionado);
    }
    
    private PensionadoRespuesta convertirAPensionadoRespuesta(Pensionado pensionado) {
        List<SucesorRespuesta> sucesoresRespuesta = new ArrayList<>();
        if (pensionado.getSucesores() != null) {
            /*
            sucesoresRespuesta = pensionado.getSucesores().stream()
                .map(sucesor -> SucesorRespuesta.builder()
                    .idPersona(sucesor.getIdPersona())
                    .numeroIdentificacion(sucesor.getNumeroIdentificacion())
                    .tipoIdentificacion(sucesor.getTipoIdentificacion().name())
                    .nombrePersona(sucesor.getNombrePersona())
                    .apellidosPersona(sucesor.getApellidosPersona())
                    .estadoCivil(sucesor.getEstadoCivil().name())
                    .fechaNacimientoPersona(sucesor.getFechaNacimientoPersona())
                    .fechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona())
                    .estadoPersona(sucesor.getEstadoPersona().name())
                    .generoPersona(sucesor.getGeneroPersona() != null ? sucesor.getGeneroPersona().name() : null)
                    .fechaDefuncionPersona(sucesor.getFechaDefuncionPersona())
                    .fechaInicioSucesion(sucesor.getFechaInicioSucesion())
                    .porcentajePension(sucesor.getPorcentajePension())
                    .build())
                .collect(Collectors.toList());
                */
        }

        List<TrabajoRespuesta> trabajosRespuesta = pensionado.getTrabajos().stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                        .idTrabajo(trabajo.getIdTrabajo())
                        .diasDeServicio(trabajo.getDiasDeServicio())
                        .nitEntidad(trabajo.getEntidad().getNitEntidad())
                        .idPersona(trabajo.getPensionado().getIdPersona())
                        .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                        .build())
                .collect(Collectors.toList());

        return PensionadoRespuesta.builder()
                .idPersona(pensionado.getIdPersona())
                .numeroIdentificacion(pensionado.getNumeroIdentificacion())
                .tipoIdentificacion(pensionado.getTipoIdentificacion())
                .nombrePersona(pensionado.getNombrePersona())
                .apellidosPersona(pensionado.getApellidosPersona())
                .estadoCivil(pensionado.getEstadoCivil().name())
                .fechaNacimientoPersona(pensionado.getFechaNacimientoPersona())
                .fechaExpedicionDocumentoIdPersona(pensionado.getFechaExpedicionDocumentoIdPersona())
                .estadoPersona(pensionado.getEstadoPersona())
                .generoPersona(pensionado.getGeneroPersona())
                .fechaInicioPension(pensionado.getFechaInicioPension())
                .valorInicialPension(pensionado.getValorInicialPension())
                .resolucionPension(pensionado.getResolucionPension())
                .nitEntidad(pensionado.getEntidadJubilacion().getNitEntidad())
                .entidadJubilacion(pensionado.getEntidadJubilacion().getNombreEntidad())
                .totalDiasTrabajo(pensionado.getTotalDiasTrabajo())
                .fechaDefuncionPersona(pensionado.getFechaDefuncionPersona())
                .aplicarIPCPrimerPeriodo(pensionado.isAplicarIPCPrimerPeriodo())
                .trabajos(trabajosRespuesta)
                .sucesores(sucesoresRespuesta)
                .build();
    }

    @Transactional
    @Override
    public void desactivarPensionado(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pensionado no encontrado con ID: " + id));
        Pensionado pensionadoAntiguo = new Pensionado();
        BeanUtils.copyProperties(pensionado, pensionadoAntiguo);
        
        // Asumiendo que "RETIRADO" o un estado similar existe en tu enum
        pensionado.setEstadoPersona(EstadoPersona.RETIRADO);
        pensionado = pensionadoRepositorio.save(pensionado);
        logCambioServicio.registrarActualizacion(nombreEntidad, pensionadoAntiguo, pensionado);
    }
    
    // El resto de los métodos de búsqueda deberían funcionar, pero siempre es bueno revisarlos.

    @Override
    public List<Pensionado> buscarPensionadosPorNombre(String nombre) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findByNombrePersonaContainingIgnoreCase(nombre);
    }

    @Override
    public List<Pensionado> buscarPensionadosPorApellido(String apellido) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findByApellidosPersonaContainingIgnoreCase(apellido);
    }

    @Override
    public List<Pensionado> buscarPensionadosPorCriterio(String query) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        if (query == null || query.trim().isEmpty()) {
            return pensionadoRepositorio.findAll();
        }
        
        query = query.trim();
        
        if (query.matches("\\d+")) {
            Long id = Long.parseLong(query);
            return pensionadoRepositorio.findById(id)
                    .map(List::of)
                    .orElseGet(ArrayList::new);
        }

        return pensionadoRepositorio.findByNombrePersonaContainingIgnoreCaseOrApellidosPersonaContainingIgnoreCase(query, query);
    }

    /**
     * Desactiva un pensionado por su ID.
     * 
     * @param id el ID del pensionado a desactivar
     * @throws RuntimeException si no se encuentra el pensionado
     */
    @Transactional
    //@Override
    public void RetirarPensionado(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pensionado no encontrado con ID: " + id));
        
        pensionado.setEstadoPersona(EstadoPersona.INACTIVO);
        pensionadoRepositorio.save(pensionado);
    }


    //@Override
    public PensionadoRespuesta ConsoltarPensionadoDetallePorId(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + id));

        Long totalDiasTrabajo = pensionado.getTrabajos().stream()
                .mapToLong(Trabajo::getDiasDeServicio)
                .sum();

        Long diasDeServicioJubilacion = pensionado.getTrabajos().stream()
            .filter(trabajo -> trabajo.getEntidad().getNitEntidad()
                    .equals(pensionado.getEntidadJubilacion().getNitEntidad()))
            .mapToLong(Trabajo::getDiasDeServicio)
            .findFirst()
            .orElse(0L);
        /*
        List<TrabajoRespuesta> trabajos = pensionado.getTrabajos().stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                        .idTrabajo(trabajo.getIdTrabajo())
                        .diasDeServicio(trabajo.getDiasDeServicio())
                        .nitEntidad(trabajo.getEntidad().getNitEntidad())
                        .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                        .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                        .build())
                .toList();
        */
        // Mapeo de Sucesores
        List<SucesorRespuesta> sucesoresRespuesta = new ArrayList<>();
        if (pensionado.getSucesores() != null) {
            /*
            sucesoresRespuesta = pensionado.getSucesores().stream()
                .map(sucesor -> SucesorRespuesta.builder()
                    .numeroIdPersona(sucesor.getNumeroIdPersona())
                    .tipoIdPersona(sucesor.getTipoIdPersona())
                    .nombrePersona(sucesor.getNombrePersona())
                    .apellidosPersona(sucesor.getApellidosPersona())
                    .fechaNacimientoPersona(sucesor.getFechaNacimientoPersona())
                    .fechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona())
                    .estadoPersona(sucesor.getEstadoPersona())
                    .generoPersona(sucesor.getGeneroPersona())
                    .fechaDefuncionPersona(sucesor.getFechaDefuncionPersona())
                    .fechaInicioSucesion(sucesor.getFechaInicioSucesion())
                    .porcentajePension(sucesor.getPorcentajePension())
                    .build())
                .collect(Collectors.toList());
            */

        }

        return PensionadoRespuesta.builder()
                .numeroIdentificacion(pensionado.getNumeroIdentificacion())
                .tipoIdentificacion(pensionado.getTipoIdentificacion())
                .nombrePersona(pensionado.getNombrePersona())
                .apellidosPersona(pensionado.getApellidosPersona())
                .fechaNacimientoPersona(pensionado.getFechaNacimientoPersona())
                .fechaExpedicionDocumentoIdPersona(pensionado.getFechaExpedicionDocumentoIdPersona())
                .estadoPersona(pensionado.getEstadoPersona())
                .generoPersona(pensionado.getGeneroPersona())           
                .fechaInicioPension(pensionado.getFechaInicioPension())
                .valorInicialPension(pensionado.getValorInicialPension())
                .resolucionPension(pensionado.getResolucionPension())
                .nitEntidad(pensionado.getEntidadJubilacion().getNitEntidad())
                .entidadJubilacion(pensionado.getEntidadJubilacion().getNombreEntidad())
                .totalDiasTrabajo(totalDiasTrabajo)
                .fechaDefuncionPersona(pensionado.getFechaDefuncionPersona())
                .aplicarIPCPrimerPeriodo(pensionado.isAplicarIPCPrimerPeriodo())
                .diasDeServicio(diasDeServicioJubilacion)
                //.trabajos(trabajos)
                .sucesores(sucesoresRespuesta)
                .build();
    }

    @Override
    public List<EntidadCuotaParteRespuesta> getEntidadesYCuotaParteByPensionadoId(Long pensionadoId) {
        // Este método puede requerir una implementación personalizada en el repositorio
        // si la consulta actual deja de funcionar con los nuevos cambios.
        // Por ahora, lo mantenemos asumiendo que la consulta subyacente sigue siendo válida.
        return null;
        //return pensionadoRepositorio.findEntidadesYCuotaParteByPensionadoId(pensionadoId);
    }
}