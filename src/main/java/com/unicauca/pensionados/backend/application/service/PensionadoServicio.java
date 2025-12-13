package com.unicauca.pensionados.backend.application.service;


import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.application.service.interfaces.IPensionadoServicio;
import com.unicauca.pensionados.backend.domain.model.enums.TipoPension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;

import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PersonaRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.RegistroPensionadoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.EntidadCuotaParteRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.PensionadoRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.SucesorRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.TrabajoRespuesta;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class PensionadoServicio implements IPensionadoServicio {

    private final PersonaRepositorio personaRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    @Autowired
    private ILogCambioServicio logCambioServicio;
    private final String nombreEntidad = "PENSIONADO";

    public PensionadoServicio(PersonaRepositorio personaRepositorio,
                              PensionadoRepositorio pensionadoRepositorio,
                              EntidadRepositorio entidadRepositorio) {
        this.personaRepositorio = personaRepositorio;
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadRepositorio = entidadRepositorio;
    }

    
    @Transactional
    @Override
    public void registrarPensionado(RegistroPensionadoPeticion request) {
        if (personaRepositorio.existsByTipoIdentificacionAndNumeroIdentificacion(request.getTipoIdentificacion(), request.getNumeroIdentificacion())) {
            throw new RuntimeException("Ya existe una persona con ese tipo y número de identificación");
        }

        Entidad entidadJubilacion = entidadRepositorio.findByNit(request.getNitEntidad().toString())
                .orElseThrow(() -> new RuntimeException("La Entidad de jubilación no se encuentra registrada"));

        TipoPension tipoPension;
        try{
             tipoPension = TipoPension.valueOf(request.getTipoPension().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new RuntimeException("Tipo de pension no valido");
        }

        // Crear y guardar la entidad Pensionado usando los campos correctos
        Pensionado pensionado = new Pensionado();
        pensionado.setCedula(request.getNumeroIdentificacion().toString());
        pensionado.setFechaExpedicionCedula(request.getFechaExpedicionDocumentoIdPersona());
        pensionado.setNombre(request.getNombrePersona());
        pensionado.setApellidos(request.getApellidosPersona());
        pensionado.setFechaNacimiento(request.getFechaNacimientoPersona());
        pensionado.setTelefono(""); // Campo requerido, podría agregarse al DTO
        pensionado.setCorreo(""); // Campo requerido, podría agregarse al DTO
        pensionado.setEntidadJubilacion(entidadJubilacion.getName());
        pensionado.setEntityNit(entidadJubilacion.getNit());
        pensionado.setEntityId(entidadJubilacion.getIdEntidad().toString());
        pensionado.setDiasTrabajadosEntidad(request.getDiasDeServicio() != null ? request.getDiasDeServicio().intValue() : 0);
        pensionado.setDiasTotalesTrabajados(request.getTotalDiasTrabajo() != null ? request.getTotalDiasTrabajo().intValue() : 0);
        pensionado.setPorcentajeCuota(java.math.BigDecimal.ZERO);
        pensionado.setTipoJubilacion(tipoPension);
        pensionado.setValorPension(request.getValorInicialPension());
        pensionado.setEstado(request.getEstadoPersona() != null ? request.getEstadoPersona() : EstadoPersona.Activo);
        pensionado.setFechaFallecimiento(request.getFechaDefuncionPersona());

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

        Entidad entidadJubilacion = entidadRepositorio.findByNit(request.getNitEntidad().toString())
                .orElseThrow(() -> new RuntimeException("La Entidad de jubilación no se encuentra registrada"));
        Pensionado pensionadoAntiguo = new Pensionado();
        BeanUtils.copyProperties(pensionadoExistente, pensionadoAntiguo);

        pensionadoExistente.setNombre(request.getNombrePersona());
        pensionadoExistente.setApellidos(request.getApellidosPersona());
        pensionadoExistente.setFechaNacimiento(request.getFechaNacimientoPersona());
        pensionadoExistente.setFechaExpedicionCedula(request.getFechaExpedicionDocumentoIdPersona());
        pensionadoExistente.setEstado(request.getEstadoPersona());
        pensionadoExistente.setFechaFallecimiento(request.getFechaDefuncionPersona());
        pensionadoExistente.setValorPension(request.getValorInicialPension());
        pensionadoExistente.setEntidadJubilacion(entidadJubilacion.getName());
        pensionadoExistente.setEntityNit(entidadJubilacion.getNit());
        pensionadoExistente.setEntityId(entidadJubilacion.getIdEntidad().toString());

        pensionadoExistente = pensionadoRepositorio.save(pensionadoExistente);
        logCambioServicio.registrarActualizacion(nombreEntidad, pensionadoAntiguo, pensionadoExistente);
        
    }

    @Override
    public List<Pensionado> listarPensionados() {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findAll();
    }

    @Override
    public Pensionado buscarPensionadoPorId(Long id) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + id));
    }

    @Transactional
    @Override
    public void desactivarPensionado(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pensionado no encontrado con ID: " + id));
        Pensionado pensionadoAntiguo = new Pensionado();
        BeanUtils.copyProperties(pensionado, pensionadoAntiguo);
        
        pensionado.setEstado(EstadoPersona.Retirado);
        pensionado = pensionadoRepositorio.save(pensionado);
        logCambioServicio.registrarActualizacion(nombreEntidad, pensionadoAntiguo, pensionado);
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