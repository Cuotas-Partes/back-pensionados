package com.unicauca.pensionados.backend.application.service;

import java.util.List;
import java.util.stream.Collectors;

import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.application.service.interfaces.ISucesorServicio;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PersonaRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.SucesorRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.domain.model.entity.Sucesor;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.application.dto.request.RegistroSucesorPeticion;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class SucesorServicio implements ISucesorServicio {
    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private SucesorRepositorio sucesorRepositorio;
    @Autowired
    private PensionadoRepositorio pensionadoRepositorio;
    @Autowired
    private ILogCambioServicio logCambioServicio;
    private final String nombreEntidad = "SUCESOR";

    /**
     * Registra un sucesor.
     * 
     * @param request Objeto que contiene la información del sucesor a registrar.
     * @throws RuntimeException si el número de identificación ya está registrado o
     *                          si la entidad no está registrada.
     * @throws Exception        si ocurre un error al registrar el sucesor.
     */
    @Transactional
    @Override
    public void registrarSucesor(RegistroSucesorPeticion request) {
        // ==================== CORRECCIÓN 1: Validación de Existencia ====================
        // Se valida usando el método correcto del repositorio para el tipo y número de ID.
        if (personaRepositorio.existsByTipoIdentificacionAndNumeroIdentificacion(request.getTipoIdentificacion(), request.getNumeroIdentificacion())) {
            throw new RuntimeException("Ya existe una persona con ese tipo y número de identificación");
        }
        
        // La búsqueda del pensionado por su ID primario es correcta.
        Pensionado pensionado = pensionadoRepositorio.findById(request.getPensionado())
                .orElseThrow(() -> new RuntimeException("El pensionado no está registrado"));

        // ==================== CORRECCIÓN 2: Creación del Sucesor ====================
        // Se usan los nuevos setters y se añade el campo 'estadoCivil'.
        Sucesor sucesor = new Sucesor();
        sucesor.setNumeroIdentificacion(request.getNumeroIdentificacion());
        sucesor.setTipoIdentificacion(request.getTipoIdentificacion());
        sucesor.setNombrePersona(request.getNombrePersona());
        sucesor.setApellidosPersona(request.getApellidosPersona());
        sucesor.setEstadoCivil(request.getEstadoCivil()); // Campo nuevo
        sucesor.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        sucesor.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        sucesor.setEstadoPersona(request.getEstadoPersona());
        sucesor.setGeneroPersona(request.getGeneroPersona());
        sucesor.setFechaInicioSucesion(request.getFechaInicioSucesion());
        sucesor.setPorcentajePension(request.getPorcentajePension());
        sucesor.setPensionado(pensionado);
        
        logCambioServicio.registrarCreacion(nombreEntidad, sucesorRepositorio.save(sucesor));
    }

    /**
     * Lista todos los sucesores registrados.
     * 
     * @return Lista de sucesores registrados.
     */
    @Override
    public List<RegistroSucesorPeticion> listaSucesores() {
        List<Sucesor> sucesores = sucesorRepositorio.findAll();
        logCambioServicio.registrarConsulta(nombreEntidad);
        return sucesores.stream().map(sucesor -> {
            // ==================== CORRECCIÓN 3: Mapeo a DTO ====================
            // Se usan los nuevos getters para poblar el DTO de respuesta.
            RegistroSucesorPeticion request = new RegistroSucesorPeticion();
            request.setNumeroIdentificacion(sucesor.getNumeroIdentificacion());
            request.setTipoIdentificacion(sucesor.getTipoIdentificacion());
            request.setNombrePersona(sucesor.getNombrePersona());
            request.setApellidosPersona(sucesor.getApellidosPersona());
            request.setEstadoCivil(sucesor.getEstadoCivil()); // Campo nuevo
            request.setFechaNacimientoPersona(sucesor.getFechaNacimientoPersona());
            request.setFechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona());
            request.setEstadoPersona(sucesor.getEstadoPersona());
            request.setGeneroPersona(sucesor.getGeneroPersona());
            request.setFechaInicioSucesion(sucesor.getFechaInicioSucesion());
            // Se obtiene el ID primario del pensionado.
            request.setPensionado(sucesor.getPensionado().getIdPersona()); 
            request.setPorcentajePension(sucesor.getPorcentajePension());
            return request;
        }).collect(Collectors.toList()); // Usar .collect(Collectors.toList()) para compatibilidad
    }

    /**
     * Obtiene un sucesor por su ID.
     * 
     * @param id SucesorId del sucesor a obtener.
     * @return Objeto que contiene la información del sucesor.
     * @throws RuntimeException si el sucesor no está registrado.
     * @throws Exception        si ocurre un error al obtener el sucesor.
     */
    @Override
    public RegistroSucesorPeticion obtenerSucesorPorId(Long id) {
        Sucesor sucesor = sucesorRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("El sucesor no está registrado"));
        logCambioServicio.registrarConsulta(nombreEntidad);

        // ==================== CORRECCIÓN 4: Mapeo a DTO ====================
        RegistroSucesorPeticion request = new RegistroSucesorPeticion();
        request.setNumeroIdentificacion(sucesor.getNumeroIdentificacion());
        request.setTipoIdentificacion(sucesor.getTipoIdentificacion());
        request.setNombrePersona(sucesor.getNombrePersona());
        request.setApellidosPersona(sucesor.getApellidosPersona());
        request.setEstadoCivil(sucesor.getEstadoCivil()); // Campo nuevo
        request.setFechaNacimientoPersona(sucesor.getFechaNacimientoPersona());
        request.setFechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona());
        request.setEstadoPersona(sucesor.getEstadoPersona());
        request.setGeneroPersona(sucesor.getGeneroPersona());
        request.setFechaInicioSucesion(sucesor.getFechaInicioSucesion());
        request.setPensionado(sucesor.getPensionado().getIdPersona());
        request.setPorcentajePension(sucesor.getPorcentajePension());

        return request;
    }

    /**
     * Edita los datos de un sucesor existente.
     * 
     * @param id      El ID del sucesor que se desea editar. Este ID se utiliza para
     *                buscar el sucesor en la base de datos.
     * @param request Un objeto de tipo RegistroSucesorPeticion que contiene los
     *                nuevos datos que se desean actualizar en el sucesor.
     * @throws RuntimeException Si el sucesor con el ID especificado no está
     *                          registrado en la base de datos.
     */
    @Override
    public void editarSucesor(Long id, RegistroSucesorPeticion request) {
        Sucesor sucesor = sucesorRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("El sucesor no está registrado"));
        Sucesor sucesorAntiguo = new Sucesor();
        BeanUtils.copyProperties(sucesor, sucesorAntiguo);

        // ==================== CORRECCIÓN 5: Actualización de Campos ====================
        // Se usan los setters correctos y se añade el nuevo campo.
        // No se permite cambiar el tipo o número de identificación en la edición.
        sucesor.setNombrePersona(request.getNombrePersona());
        sucesor.setApellidosPersona(request.getApellidosPersona());
        sucesor.setEstadoCivil(request.getEstadoCivil()); // Campo nuevo
        sucesor.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        sucesor.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        sucesor.setEstadoPersona(request.getEstadoPersona());
        sucesor.setGeneroPersona(request.getGeneroPersona());
        sucesor.setFechaInicioSucesion(request.getFechaInicioSucesion());

        sucesor = sucesorRepositorio.save(sucesor);
        logCambioServicio.registrarActualizacion(nombreEntidad, sucesorAntiguo, sucesor);
    }


    /**
     * Elimina un sucesor por su ID.
     * 
     * @param id SucesorId del sucesor a eliminar.
     * @throws RuntimeException si el sucesor no está registrado.
     * @throws Exception        si ocurre un error al eliminar el sucesor.
     */
    @Override
    public void eliminarSucesor(Long id) {
        Sucesor sucesor = sucesorRepositorio.findById(id).orElseThrow(() -> new RuntimeException("El sucesor no está registrado"));
        // Eliminar el sucesor
        sucesorRepositorio.deleteById(id);
        //Registrar log de eliminacion
        logCambioServicio.registrarEliminacion(nombreEntidad,sucesor);
    }
}