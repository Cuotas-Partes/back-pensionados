package com.unicauca.pensionados.backend.application.service;


import com.unicauca.pensionados.backend.application.dto.request.pensionado.RegistroPensionadoPeticion;
import com.unicauca.pensionados.backend.application.dto.request.pensionado.RegistroResolucionPensionadoPeticion;
import com.unicauca.pensionados.backend.application.dto.request.pensionado.RegistroSucesorPensionadoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.pensionado.PensionadoDTO;
import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.application.service.interfaces.IPensionadoServicio;
import com.unicauca.pensionados.backend.domain.exception.BusinessValidationException;
import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Resolucion;
import com.unicauca.pensionados.backend.domain.model.entity.Sucesor;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoSustituto;
import com.unicauca.pensionados.backend.domain.model.mappers.pensionado.PensionadoMapper;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.ResolucionRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.SucesorRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PensionadoServicio implements IPensionadoServicio {

    private final PensionadoRepositorio pensionadoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final ResolucionRepositorio resolucionRepositorio;
    private final SucesorRepositorio sucesorRepositorio;

    @Autowired
    private ILogCambioServicio logCambioServicio;
    private final String nombreEntidad = "PENSIONADO";

    public PensionadoServicio(PensionadoRepositorio pensionadoRepositorio,
                              EntidadRepositorio entidadRepositorio,
                              ResolucionRepositorio resolucionRepositorio,
                              SucesorRepositorio sucesorRepositorio) {
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadRepositorio = entidadRepositorio;
        this.resolucionRepositorio = resolucionRepositorio;
        this.sucesorRepositorio = sucesorRepositorio;
    }

    
    @Transactional
    @Override
    public void registrarPensionado(RegistroPensionadoPeticion request) {
        if (pensionadoRepositorio.findByCedula(request.getCedula()).isPresent()) {
            throw new BusinessValidationException("Ya existe un pensionado con la cédula: " + request.getCedula());
        }

        Entidad entidadJubilacion = entidadRepositorio.findById(request.getEntityId())
                .orElseThrow(() -> new RecursoNoEncontrado("La Entidad de jubilación no se encuentra registrada"));

        Pensionado pensionado = new Pensionado();
        aplicarDatosPensionado(pensionado, entidadJubilacion, request);

        pensionado.setResoluciones(construirResoluciones(pensionado, request.getResoluciones()));
        pensionado.setSustitutos(construirSustitutos(pensionado, request.getSustitutos()));
        pensionado.setTieneSustituto(pensionado.getSustitutos() != null && !pensionado.getSustitutos().isEmpty());

        if (pensionado.getSustitutos() != null && !pensionado.puedeAgregarSustituto()) {
            throw new BusinessValidationException("No se pueden registrar más de 2 sustitutos activos");
        }

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
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró el pensionado con ID: " + idPersona));

        if (!pensionadoExistente.getCedula().equalsIgnoreCase(request.getCedula())
                && pensionadoRepositorio.findByCedula(request.getCedula()).isPresent()) {
            throw new BusinessValidationException("Ya existe un pensionado con la cédula: " + request.getCedula());
        }

        Entidad entidadJubilacion = entidadRepositorio.findById(request.getEntityId())
                .orElseThrow(() -> new RecursoNoEncontrado("La Entidad de jubilación no se encuentra registrada"));

        Pensionado pensionadoAntiguo = new Pensionado();
        BeanUtils.copyProperties(pensionadoExistente, pensionadoAntiguo);

        aplicarDatosPensionado(pensionadoExistente, entidadJubilacion, request);

        // Reemplazo total (simple): si quieres "patch" fino por IDs, eso es otra capa.
        pensionadoExistente.setResoluciones(construirResoluciones(pensionadoExistente, request.getResoluciones()));
        pensionadoExistente.setSustitutos(construirSustitutos(pensionadoExistente, request.getSustitutos()));
        pensionadoExistente.setTieneSustituto(pensionadoExistente.getSustitutos() != null && !pensionadoExistente.getSustitutos().isEmpty());

        if (pensionadoExistente.getSustitutos() != null && !pensionadoExistente.puedeAgregarSustituto()) {
            throw new BusinessValidationException("No se pueden registrar más de 2 sustitutos activos");
        }

        pensionadoExistente = pensionadoRepositorio.save(pensionadoExistente);
        logCambioServicio.registrarActualizacion(nombreEntidad, pensionadoAntiguo, pensionadoExistente);
    }

    @Override
    public List<PensionadoDTO> listarPensionados() {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findAll()
                .stream()
                .map(PensionadoMapper::toDTO)
                .toList();
    }

    @Override
    public List<PensionadoDTO> listarPensionadoPorEntidad(Long entidadId) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findByEntityId(entidadId)
                .stream()
                .map(PensionadoMapper::toDTO)
                .toList();
    }
    @Override
    public Pensionado buscarPensionadoPorId(Long id) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró el pensionado con ID: " + id));
    }

    /**
     * Eliminación lógica (soft delete).
     *
     * Se delega a {@link #desactivarPensionado(Long)} para evitar duplicación de lógica.
     * No se hace delete físico porque el pensionado puede estar referenciado por pagos/liquidaciones.
     */
    @Transactional
    @Override
    public void eliminarPensionado(Long id) {
        desactivarPensionado(id);
    }

    @Transactional
    @Override
    public void desactivarPensionado(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Pensionado no encontrado con ID: " + id));
        Pensionado pensionadoAntiguo = new Pensionado();
        BeanUtils.copyProperties(pensionado, pensionadoAntiguo);

        pensionado.setEstado(EstadoPersona.Retirado);
        pensionado = pensionadoRepositorio.save(pensionado);
        logCambioServicio.registrarActualizacion(nombreEntidad, pensionadoAntiguo, pensionado);
    }

    private void aplicarDatosPensionado(Pensionado pensionado, Entidad entidadJubilacion, RegistroPensionadoPeticion request) {
        pensionado.setCedula(request.getCedula());
        pensionado.setFechaExpedicionCedula(request.getFechaExpedicionCedula());
        pensionado.setNombre(request.getNombre());
        pensionado.setApellidos(request.getApellidos());
        pensionado.setFechaNacimiento(request.getFechaNacimiento());
        pensionado.setTelefono(request.getTelefono());
        pensionado.setCorreo(request.getCorreo());

        pensionado.setEntidadJubilacion(entidadJubilacion.getName());
        pensionado.setEntityNit(entidadJubilacion.getNit());
        pensionado.setEntityId(entidadJubilacion.getIdEntidad());

        pensionado.setDiasTrabajadosEntidad(request.getDiasTrabajadosEntidad());
        pensionado.setDiasTotalesTrabajados(request.getDiasTotalesTrabajados());
        pensionado.setTipoJubilacion(request.getTipoJubilacion());
        pensionado.setValorPensionActual(BigDecimal.valueOf(request.getValorPensionActual()));

        pensionado.setEstado(request.getEstado() != null ? request.getEstado() : EstadoPersona.Activo);
        pensionado.setFechaFallecimiento(request.getFechaFallecimiento());
    }

    private List<Resolucion> construirResoluciones(Pensionado pensionado, List<RegistroResolucionPensionadoPeticion> peticiones) {
        if (peticiones == null) {
            return null;
        }

        List<Resolucion> resoluciones = new ArrayList<>();
        for (RegistroResolucionPensionadoPeticion p : peticiones) {
            Optional<Resolucion> existente = resolucionRepositorio.findByNumeroResolucion(p.getNumeroResolucion());
            if (existente.isPresent()) {
                Resolucion rExistente = existente.get();
                if (rExistente.getPensionado() != null && pensionado.getIdPensionado() != null
                        && !rExistente.getPensionado().getIdPensionado().equals(pensionado.getIdPensionado())) {
                    throw new BusinessValidationException("El número de resolución ya existe: " + p.getNumeroResolucion());
                }
            }

            Resolucion r = new Resolucion();
            r.setNumeroResolucion(p.getNumeroResolucion());
            r.setFechaResolucion(p.getFechaResolucion());
            r.setValorResolucion(p.getValorResolucion());
            r.setEstado(p.getEstado() != null ? p.getEstado() : EstadoResolucion.VIGENTE);
            r.setTipoResolucion(p.getTipoResolucion());
            r.setPensionado(pensionado);
            resoluciones.add(r);
        }

        return resoluciones;
    }

    private List<Sucesor> construirSustitutos(Pensionado pensionado, List<RegistroSucesorPensionadoPeticion> peticiones) {
        if (peticiones == null) {
            return null;
        }

        List<Sucesor> sucesores = new ArrayList<>();
        for (RegistroSucesorPensionadoPeticion p : peticiones) {
            Sucesor s = new Sucesor();

            // Campos heredados de Persona (OBLIGATORIOS)
            s.setNumeroDocumento(p.getNumeroIdentificacion());
            s.setTipoDocumento(p.getTipoIdentificacion());
            s.setNombreCompleto(p.getNombrePersona() + " " + p.getApellidoPersona());

            // Campos específicos de Sucesor
            s.setNumeroDocumento(p.getNumeroIdentificacion());
            s.setTipoDocumento(p.getTipoIdentificacion());
            s.setNombreCompleto(p.getNombrePersona() + " " + p.getApellidoPersona());
            s.setFechaInicio(p.getFechaInicio());
            s.setFechaFin(p.getFechaFin());
            s.setPorcentajePension(p.getPorcentajePension());
            s.setPensionadoSustituido(pensionado);

            if (p.getNumeroResolucionNombramiento() != null && !p.getNumeroResolucionNombramiento().isBlank()) {
                Resolucion resolucionNombramiento = resolucionRepositorio.findByNumeroResolucion(p.getNumeroResolucionNombramiento())
                        .orElseThrow(() -> new RecursoNoEncontrado(
                                "No se encontró resolución de nombramiento: " + p.getNumeroResolucionNombramiento()));
                s.setResolucionNombramiento(resolucionNombramiento);
            }

            sucesores.add(s);
        }

        return sucesores;
    }
    
    // El resto de los métodos de búsqueda deberían funcionar, pero siempre es bueno revisarlos.

    @Override
    public List<Pensionado> buscarPensionadosPorNombre(String nombre) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Pensionado> buscarPensionadosPorApellido(String apellido) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findByApellidosContainingIgnoreCase(apellido);
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

        return pensionadoRepositorio.findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(query, query);
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
        
        pensionado.setEstado(EstadoPersona.Inactivo);
        pensionadoRepositorio.save(pensionado);
    }
}